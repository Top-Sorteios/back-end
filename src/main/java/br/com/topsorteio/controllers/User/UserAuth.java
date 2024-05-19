package br.com.topsorteio.controllers.User;

import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuth {

    @Autowired
    private iUserRepository repository;

    @PostMapping
    @RequestMapping("/registrar")
    public ResponseEntity registrarUsuario(){
        return null;
    }

}
