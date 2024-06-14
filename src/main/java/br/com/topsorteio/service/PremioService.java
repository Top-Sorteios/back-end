package br.com.topsorteio.service;

import br.com.topsorteio.dtos.PremioTotalParticipantesResponseDTO;
import br.com.topsorteio.dtos.ProcedureParticipandoSorteioResponseDTO;
import br.com.topsorteio.entities.TurmaModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.repositories.IMarcaRepository;
import br.com.topsorteio.repositories.iTurmaRepository;
import br.com.topsorteio.repositories.iUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PremioService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private iTurmaRepository turmaRepository;

    @Autowired
    private iUserRepository userRepository;

    public ResponseEntity sortearUsuario(boolean SorteioSurpresa){
        try {
            int isSorteioSurpresa = 0;

            if(SorteioSurpresa)
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
                Random random = new Random();
                ProcedureParticipandoSorteioResponseDTO sorteado = usuariosParticipantesDTO.get(random.nextInt(usuariosParticipantesDTO.size()));

                Optional<TurmaModel> turmaSorteado = turmaRepository.findByNome(sorteado.turma());

                if(turmaSorteado.isEmpty()) return new ResponseEntity<>("Turma do Sorteado não Encontrado", HttpStatus.BAD_REQUEST);

                turmaSorteado.get().setParticipandoSorteio(false);

//                turmaRepository.save(turmaSorteado.get());

                return new ResponseEntity<>(sorteado, HttpStatus.OK);
            }

            return new ResponseEntity<>("Ninguem participando do Sorteio", HttpStatus.BAD_REQUEST);
        }catch(Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity contarQuantosParticipantes(boolean SorteioSurpresa){
        int isSorteioSurpresa = 0;

        if(SorteioSurpresa)
            isSorteioSurpresa = 1;

        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("SP_ObterUsuariosParaSorteio_V2_S");
        storedProcedure.registerStoredProcedureParameter("SORTEIO_SURPRESA", String.class, ParameterMode.IN);
        storedProcedure.setParameter("SORTEIO_SURPRESA", isSorteioSurpresa);


        storedProcedure.execute();

        List<Object[]> queryResult = storedProcedure.getResultList();

        if(queryResult.isEmpty()) return new ResponseEntity<>("Sem Participantes", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new PremioTotalParticipantesResponseDTO(queryResult.stream().count()), HttpStatus.OK);
    }
}
