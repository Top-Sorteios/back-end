package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.ObterHistoricoSorteioPorTurmaRequestDTO;
import br.com.topsorteio.dtos.SorteioResquestDTO;
import br.com.topsorteio.dtos.isSorteioSurpresaRequestDTO;
import br.com.topsorteio.entities.HistoricoSorteioModel;
import br.com.topsorteio.service.SorteioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sorteios")
public class SorteioController {

    @Autowired
    private SorteioService repository;

    @PostMapping("/sortear")
    public ResponseEntity sortearPremio(@RequestBody SorteioResquestDTO request){
        return repository.sortearUsuario(request);
    }

    @PostMapping("/participantes-do-sorteio")
    public ResponseEntity statusDoSorteio(@RequestBody isSorteioSurpresaRequestDTO request){
        return repository.participantesDoSorteio(request);
    }

    @GetMapping("/sorteios-da-semana")
    public ResponseEntity sorteiosDaSemana(){
        return repository.obterSorteiosDaSemana();
    }

    @GetMapping("/historico-sorteio")
    public ResponseEntity historicoSorteio(){return repository.obterHistoricoSorteio(); }


    @PostMapping("/historico-sorteio/turma")
    public List<HistoricoSorteioModel> buscarPorTurma(@RequestBody ObterHistoricoSorteioPorTurmaRequestDTO request) {
        return repository.buscarPorTurma(request.turmaNome());
    }

}
