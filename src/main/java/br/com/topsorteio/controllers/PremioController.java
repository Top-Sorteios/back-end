package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.PremiosCadastradosResponseDTO;
import br.com.topsorteio.dtos.PremioEditRequestDTO;
import br.com.topsorteio.dtos.PremioRegisterRequestDTO;
import br.com.topsorteio.service.PremioService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/premios")
public class PremioController {

    @Autowired
    private PremioService repository;

    @GetMapping("/obter")
    public ResponseEntity<List<PremiosCadastradosResponseDTO>> obterTodosOsPremios(){
        return repository.obterTodosOsPremios();
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<?> obterPremioPorId(@PathVariable Integer id) {
        return repository.obterPremioPorId(id);
    }


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPremio(@ModelAttribute PremioRegisterRequestDTO request) throws IOException {
        return repository.registrarPremio(request);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarPremio(@PathVariable Integer id, @ModelAttribute PremioEditRequestDTO request) throws IOException{
        return repository.editarPremio(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerPremio(@PathVariable Integer id) {
        return repository.removerPremio(id);
    }

}
