package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UserController {


    @Autowired
    private UserService repository;

    @PostMapping
    @RequestMapping("/registrar")
    public ResponseEntity registrarUsuario(@RequestBody UserRegisterRequestDTO request){
        return repository.registrarUsuario(request);
    }


    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        return repository.login(request);
    }

    @GetMapping
    @RequestMapping("/obter")
    public ResponseEntity<List<UserResponseDTO>> pegarTodosOsUsuarios(){
        return repository.pegarTodosOsUsuarios();
    }

    @GetMapping
    @RequestMapping("/obter/{email}")
    public ResponseEntity acharPeloEmail(@PathVariable String email){
        return repository.acharPeloEmail(email);
    }

    @PutMapping
    @RequestMapping("/editar/senha/{email}")
    public ResponseEntity editarSenha(@PathVariable String email, @RequestBody UserEditRequestDTO request){
        return repository.editarSenha(request, email);
    }

    @PutMapping
    @RequestMapping("/primeiro-acesso")
    public ResponseEntity editarSenha(@RequestBody FirstAcessRequestDTO data){
       return repository.primeiroAcesso(data);
    }

    @PostMapping
    @RequestMapping("/esqueci-senha")
    public ResponseEntity esqueciSenha(@RequestBody EsqueciSenhaRequestDTO request){
        return repository.esqueciSenha(request);
    }

    @GetMapping
    @RequestMapping("/helloworld")
    public ResponseEntity HelloWorld(){
        return ResponseEntity.ok("Hello World");
    }


}
