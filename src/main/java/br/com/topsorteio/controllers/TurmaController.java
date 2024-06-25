package br.com.topsorteio.controllers;


import br.com.topsorteio.entities.TurmaModel;
import br.com.topsorteio.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping("/obter")
    public ResponseEntity obterTodasAsTurmas(){
        return turmaService.obterTodasAsTurmas();
    }

}
