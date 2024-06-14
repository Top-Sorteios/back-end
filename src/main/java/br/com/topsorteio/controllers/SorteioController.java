package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.IsSorteioSurpresaResquestDTO;
import br.com.topsorteio.service.SorteioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sorteios")
public class SorteioController {

    @Autowired
    private SorteioService repository;

    @PostMapping("/sortear")
    public ResponseEntity premio(@RequestBody IsSorteioSurpresaResquestDTO request){
        return repository.sortearUsuario(request);
    }

    @PostMapping("/status")
    public ResponseEntity statusDoSorteio(@RequestBody IsSorteioSurpresaResquestDTO request){
        return repository.statusDoSorteio(request);
    }



}
