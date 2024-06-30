package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.DestaqueIndexRequestDTO;
import br.com.topsorteio.dtos.DestaqueIndexResponseDTO;
import br.com.topsorteio.service.DestaqueIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/index")
public class DestaqueIndexController {

    @Autowired
    private DestaqueIndexService repository;

    @GetMapping("/obter")
    public ResponseEntity<List<DestaqueIndexResponseDTO>> obterDestaqueIndex(){
        return repository.obterDestaqueIndex();
    }

    @GetMapping ("/obter/{id}")
    public ResponseEntity obterDestaqueIndexId(@PathVariable Long id){return repository.obterDestaqueIndexPorId(id);}

    @PostMapping("/registrar")
    public ResponseEntity adicionarDestaqueIndex(@ModelAttribute DestaqueIndexRequestDTO requestDTO){return repository.adicionarDestaqueIndex(requestDTO);}

    @PutMapping("/editar/{id}")
    public ResponseEntity editarDestaqueIndex(@PathVariable Long id, @ModelAttribute DestaqueIndexRequestDTO request){
        return repository.editarDestaqueIndex(id, request);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity removerDestaqueIndex(@PathVariable Long id){ return  repository.removerDestaqueIndex(id);}

}
