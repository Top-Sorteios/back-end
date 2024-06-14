package br.com.topsorteio.controllers;

import br.com.topsorteio.service.PremioService;
import br.com.topsorteio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/sortear")
    public ResponseEntity premio(){
        return repository.sortearUsuario(false);
    }

    @GetMapping("/total-participantes")
    public ResponseEntity totalParticipantes(){
        return repository.contarQuantosParticipantes(false);
    }
}
