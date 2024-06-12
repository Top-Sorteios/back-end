package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.GetAllPremiosResponseDTO;
import br.com.topsorteio.dtos.PremioEditRequestDTO;
import br.com.topsorteio.dtos.PremioRegisterRequestDTO;
import br.com.topsorteio.service.PremioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/premios")
public class PremioController {

    @Autowired
    private PremioService repository;

    @GetMapping("/obter")
    public ResponseEntity<List<GetAllPremiosResponseDTO>> obterTodosOsPremios(){
        return repository.obterTodosOsPremios();
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<?> obterPremioPorId(@PathVariable Integer id){
        return repository.obterPremioPorId(id);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPremio(@RequestBody PremioRegisterRequestDTO request){
        return repository.registrarPremio(request);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarPremio(@PathVariable Integer id, @RequestBody PremioEditRequestDTO request){
        return repository.editarPremio(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerPremio(@PathVariable Integer id){
        return repository.removerPremio(id);
    }
}
