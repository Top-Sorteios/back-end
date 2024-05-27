package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.infra.security.TokenService;
import br.com.topsorteio.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService repository;

    @PostMapping
    @RequestMapping("/registrar")
    public ResponseEntity registrarUsuario(@RequestBody UserRegisterRequestDTO request){
        Optional<UserModel> userResponse = this.repository.findByEmail(request.email());

        if(userResponse.isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorDTO(HttpStatus.CONFLICT, 400, "Usuário já existe.", false));


        return ResponseEntity.status(HttpStatus.CREATED).body(repository.createUser(new UserModel(request)));
    }


    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        return repository.login(request);
    }

    @GetMapping
    @RequestMapping("/obter")
    public ResponseEntity<List<GetAllUserResponseDTO>> pegarTodosOsUsuarios(){
        return repository.pegarTodosOsUsuarios();
    }

    @GetMapping
    @RequestMapping("/obter/{id}")
    public ResponseEntity acharPeloId(@PathVariable Integer id){
        return repository.acharPeloID(id);
    }

    @PutMapping
    @RequestMapping("/editar/{email}")
    public ResponseEntity editarSenha(@PathVariable String email, @RequestBody UserEditRequestDTO request){
        return repository.editarSenha(email, request);
    }

//    @PutMapping
//    @RequestMapping("/primeiro-acesso")
//    public ResponseEntity editarSenha(@RequestBody FirstAcessRequestDTO data){
//        String senhaCriptografada = passwordEncoder.encode(data.senha());
//        UserModel usuarioPrimeiroAcesso = repository.primeiroAcesso(data, senhaCriptografada);
//
//        System.out.print(usuarioPrimeiroAcesso);
//
//        if(usuarioPrimeiroAcesso != null)
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new TokenResponseDTO(tokenService.generateToken(usuarioPrimeiroAcesso), true));
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Informações Inválidas ou Primeiro acesso já feito.", false));
//    }
}