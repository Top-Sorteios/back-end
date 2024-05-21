package br.com.topsorteio.controllers.User;

import br.com.topsorteio.dtos.userdto.LoginRequestDTO;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UserAuth {
    @Autowired
    private iUserRepository repository;

    @PostMapping
    @RequestMapping("/registrar")
    public ResponseEntity registrarUsuario(){
        return null;
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        Optional<UserModel> userResponse = repository.findByEmail(request.email());

        if(userResponse.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email ou Senha Inválidos");
        }

        var user = userResponse.get();

        if(user.getEmail().equals(request.email()) && user.getSenha().equals(request.senha())){
            return ResponseEntity.ok("Sucesso");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não Achou");
    }

}
