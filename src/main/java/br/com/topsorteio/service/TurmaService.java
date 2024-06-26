package br.com.topsorteio.service;

import br.com.topsorteio.dtos.FiltrarPorTurmasRequestDTO;
import br.com.topsorteio.dtos.TurmaResponseDTO;
import br.com.topsorteio.entities.TurmaModel;
import br.com.topsorteio.repositories.iTurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TurmaService {

    @Autowired
    private iTurmaRepository repository;

    public ResponseEntity obterTodasAsTurmas(){
        List<TurmaModel> turmas = repository.findAll();
        List<TurmaResponseDTO> responseTurma = new ArrayList<>();

        for(TurmaModel turma : turmas){
            responseTurma.add(new TurmaResponseDTO(turma.getId(), turma.getNome(), turma.isParticipandoSorteio(), turma.getCriadoem()));
        }

        return new ResponseEntity<>(responseTurma, HttpStatus.OK);
    }


    public ResponseEntity filtrarTurma(FiltrarPorTurmasRequestDTO data){
        List<TurmaResponseDTO> response = new ArrayList<>();

        for(String turma : data.turmas()){

            TurmaModel searchTurma = repository.findByNome(turma).get();
            response.add(new TurmaResponseDTO(searchTurma.getId(), searchTurma.getNome(), searchTurma.isParticipandoSorteio(), searchTurma.getCriadoem()));;

        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
