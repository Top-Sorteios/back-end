package br.com.topsorteio.service;

import br.com.topsorteio.dtos.ErrorDTO;
import br.com.topsorteio.dtos.IsSorteioSurpresaResquestDTO;
import br.com.topsorteio.dtos.PremioTotalParticipantesResponseDTO;
import br.com.topsorteio.dtos.ProcedureParticipandoSorteioResponseDTO;
import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.entities.SorteioModel;
import br.com.topsorteio.entities.TurmaModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.repositories.IPremioRepository;
import br.com.topsorteio.repositories.iSorteioRepository;
import br.com.topsorteio.repositories.iTurmaRepository;
import br.com.topsorteio.repositories.iUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SorteioService {

    @Autowired
    private iTurmaRepository turmaRepository;

    @Autowired
    private iUserRepository usuarioRepository;

    @Autowired
    private IPremioRepository premioRepository;

    @Autowired
    private iSorteioRepository sorteioRepository;
    @Autowired
    private EntityManager entityManager;

    public ResponseEntity sortearUsuario(IsSorteioSurpresaResquestDTO data){
        try {
            int isSorteioSurpresa = 0;

            if(data.sorteio_surpresa())
                isSorteioSurpresa = 1;


            StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("SP_ObterUsuariosParaSorteio_V2_S");
            storedProcedure.registerStoredProcedureParameter("SORTEIO_SURPRESA", String.class, ParameterMode.IN);
            storedProcedure.setParameter("SORTEIO_SURPRESA", isSorteioSurpresa);


            storedProcedure.execute();


            List<ProcedureParticipandoSorteioResponseDTO> usuariosParticipantesDTO = new ArrayList<>();
            List<Object[]> queryResult = storedProcedure.getResultList();

            for (Object[] item : queryResult) {
                ProcedureParticipandoSorteioResponseDTO dto = new ProcedureParticipandoSorteioResponseDTO((Integer) item[0],(String) (item[1]), (String) (item[2]), (String) (item[3]), (String) (item[4]));
                usuariosParticipantesDTO.add(dto);
            }

            if (!usuariosParticipantesDTO.isEmpty()) {
                UserModel emailAdm = usuarioRepository.findByEmail(data.email_administrador()).orElseThrow(() -> new EventNotFoundException("Administrador não existe"));
                PremioModel premio =  premioRepository.findByCodigoSku(data.codigo_sku()).orElseThrow(() -> new EventNotFoundException("Premio não existe."));

                Random random = new Random();
                ProcedureParticipandoSorteioResponseDTO sorteado = usuariosParticipantesDTO.get(random.nextInt(usuariosParticipantesDTO.size()));

                TurmaModel turmaSorteado = turmaRepository.findByNome(sorteado.turma()).orElseThrow(() -> new EventNotFoundException("Turma não encontrado."));
                UserModel ganhador = usuarioRepository.findByEmail(sorteado.email()).orElseThrow(() -> new EventNotFoundException("Não foi possivel encontrar o ganhador."));

                //Registro do Sorteio
                SorteioModel sorteio = new SorteioModel(premio, ganhador, emailAdm);
                ganhador.getTurma().setParticipandoSorteio(false);

                //Remover Premio da Lista
//                premioRepository.delete(premio);

                //Salvar Ganhador
               sorteioRepository.save(sorteio);


                return new ResponseEntity<>(sorteado, HttpStatus.OK);
            }

            return new ResponseEntity<>("Ninguem participando do Sorteio", HttpStatus.BAD_REQUEST);
        }catch(Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity participantesDoSorteio(IsSorteioSurpresaResquestDTO data){
        int isSorteioSurpresa = 0;

        if(data.sorteio_surpresa())
            isSorteioSurpresa = 1;

        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("SP_ObterUsuariosParaSorteio_V2_S");
        storedProcedure.registerStoredProcedureParameter("SORTEIO_SURPRESA", String.class, ParameterMode.IN);
        storedProcedure.setParameter("SORTEIO_SURPRESA", isSorteioSurpresa);


        storedProcedure.execute();

        List<Object[]> queryResult = storedProcedure.getResultList();

        if(queryResult.isEmpty()) return new ResponseEntity<>("Sem Participantes", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new PremioTotalParticipantesResponseDTO(queryResult.stream().count()), HttpStatus.OK);
    }

    public ResponseEntity obterSorteiosDaSemana(){
        try{
           List<SorteioModel> todosOsSorteios = sorteioRepository.findAll();

           return new ResponseEntity<>(todosOsSorteios, HttpStatus.OK);
        }catch(Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

}
