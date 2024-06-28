package br.com.topsorteio.service;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.*;
import br.com.topsorteio.exceptions.EventBadRequestException;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.repositories.IPremioRepository;
import br.com.topsorteio.repositories.IHistoricoSorteioRepository;
import br.com.topsorteio.repositories.iSorteioRepository;
import br.com.topsorteio.repositories.iUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class SorteioService {

    @Autowired
    private iUserRepository usuarioRepository;
    @Autowired
    private IPremioRepository premioRepository;
    @Autowired
    private iSorteioRepository sorteioRepository;
    @Autowired
    private IHistoricoSorteioRepository historicoSorteioRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional(rollbackFor = JpaSystemException.class)
    public ResponseEntity sortearUsuario(SorteioResquestDTO data) {
        try {
            int isSorteioSurpresa = 0;

            if (data.sorteio_surpresa())
                isSorteioSurpresa = 1;


            StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("SP_ObterUsuariosParaSorteio_V2_S");
            storedProcedure.registerStoredProcedureParameter("SORTEIO_SURPRESA", String.class, ParameterMode.IN);
            storedProcedure.setParameter("SORTEIO_SURPRESA", isSorteioSurpresa);

            storedProcedure.execute();

            List<ProcedureParticipandoSorteioResponseDTO> usuariosParticipantesDTO = new ArrayList<>();
            List<Object[]> queryResult = storedProcedure.getResultList();

            for (Object[] item : queryResult) {
                ProcedureParticipandoSorteioResponseDTO dto = new ProcedureParticipandoSorteioResponseDTO((Integer) item[0], (String) (item[1]), (String) (item[2]), (String) (item[3]), (String) (item[4]));
                usuariosParticipantesDTO.add(dto);
            }

            if (!usuariosParticipantesDTO.isEmpty()) {
                UserModel emailAdm = usuarioRepository.findByEmail(data.email_autenticado()).orElseThrow(() -> new EventNotFoundException("Administrador não existe"));

                Random random = new Random();
                ProcedureParticipandoSorteioResponseDTO sorteado = usuariosParticipantesDTO.get(random.nextInt(usuariosParticipantesDTO.size()));

                UserModel usuarioGanhador = usuarioRepository.findByEmail(sorteado.email()).orElseThrow(() -> new EventNotFoundException("Não foi possivel encontrar o ganhador."));

                PremioModel premio = validateSorteio(data);

                premio.subtrair(1);

                //Registro do Sorteio
                SorteioModel sorteio = new SorteioModel(premio, usuarioGanhador, usuarioGanhador.getTurma(), emailAdm, data.sorteio_surpresa());
//                ganhador.getTurma().setParticipandoSorteio(false);

                //Salvar Ganhador
                sorteioRepository.save(sorteio);


                return new ResponseEntity<>(sorteado, HttpStatus.OK);
            }
            return new ResponseEntity<>("Ninguem participando do Sorteio", HttpStatus.BAD_REQUEST);
        } catch (JpaSystemException ex) {
            throw new EventInternalServerErrorException("Algo de inesperado.");
        }
    }

    private PremioModel validateSorteio(SorteioResquestDTO data) {
        return premioRepository.findByCodigoSku(data.codigo_sku())
                .map(premio -> premio.getQuantidade() > 0 ? premio : null)
                .orElseThrow(() -> new EventBadRequestException("Esse premio não tem mais itens."));
    }

    public ResponseEntity participantesDoSorteio(isSorteioSurpresaRequestDTO data) {
        int isSorteioSurpresa = 0;

        if (data.sorteio_surpresa())
            isSorteioSurpresa = 1;

        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("SP_ObterUsuariosParaSorteio_V2_S");
        storedProcedure.registerStoredProcedureParameter("SORTEIO_SURPRESA", String.class, ParameterMode.IN);
        storedProcedure.setParameter("SORTEIO_SURPRESA", isSorteioSurpresa);

        storedProcedure.execute();

        List<Object[]> queryResult = storedProcedure.getResultList();

        if (queryResult.isEmpty()) return new ResponseEntity<>("Sem Participantes", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new PremioTotalParticipantesResponseDTO(queryResult.size()), HttpStatus.OK);
    }

    public ResponseEntity obterSorteiosDaSemana() {
        try {
            List<SorteioModel> todosOsSorteios = sorteioRepository.findAll();
            return new ResponseEntity<>(todosOsSorteios, HttpStatus.OK);
        } catch (Exception ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity obterHistoricoSorteio() {
        try {
            List<HistoricoSorteioModel> historicoSorteio = historicoSorteioRepository.findAll();
            return new ResponseEntity<>(historicoSorteio, HttpStatus.OK);
        } catch (Exception ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }

    public ResponseEntity<List<HistoricoSorteioModel>> buscarPorTurma(String turmaNome) {
        try {
            List<HistoricoSorteioModel> historicoPorTurma = historicoSorteioRepository.findByTurmaNome(turmaNome);
            if (historicoPorTurma.isEmpty()) {
                throw new EventBadRequestException("Nenhum histórico encontrado para a turma: " + turmaNome);
            }
            return new ResponseEntity<>(historicoPorTurma, HttpStatus.OK);
        } catch (Exception ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity filtrarPorTurmas(FiltrarPorTurmasRequestDTO data){
        try{
            List<FiltrarPorTurmasResponseDTO> response = new ArrayList<>();

            for(String turma : data.turmas()){

                List<HistoricoSorteioModel> searchTurmas = historicoSorteioRepository.findByTurmaNome(turma);

                for(HistoricoSorteioModel searchTurma : searchTurmas){
                    response.add(new FiltrarPorTurmasResponseDTO(
                            searchTurma.getGanhadorNome(),
                            searchTurma.getGanhadorEmail(),
                            searchTurma.getGanhadorDataNascimento(),
                            searchTurma.getPremioNome(),
                            searchTurma.getPremioSku(),
                            searchTurma.getPremioImagem(),
                            searchTurma.getPremioDescricao(),
                            searchTurma.isPremioSurpresa(),
                            searchTurma.getTurmaNome(),
                            searchTurma.getMarcaNome(),
                            searchTurma.getSorteadoEm(),
                            searchTurma.getSorteadoPor(),
                            searchTurma.getCriadoEm()
                    ));
                }

            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

}
