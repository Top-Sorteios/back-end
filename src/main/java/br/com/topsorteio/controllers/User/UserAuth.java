package br.com.topsorteio.controllers.User;


import br.com.topsorteio.dtos.userdto.LoginRequestDTO;
import br.com.topsorteio.dtos.userdto.LoginResponseDTO;
import br.com.topsorteio.entities.user.UserModel;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.infra.security.TokenService;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    TokenService tokenService;

    @PostMapping
    @RequestMapping("/registrar")
    public ResponseEntity registrarUsuario(){
        return null;
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        UserModel userResponse = this.repository.findByEmail(request.email()).orElseThrow(() -> new EventNotFoundException("Email ou Senha inválidos."));

        String token = this.tokenService.generateToken(userResponse);

        if(userResponse.getEmail().equals(request.email()) && userResponse.getSenha().equals(request.senha())){
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token, true));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não Achou");
    }

}
