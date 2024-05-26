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
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping
    @RequestMapping("/registrar")
    public ResponseEntity registrarUsuario(@RequestBody UserRegisterRequestDTO request){
        Optional<UserModel> userResponse = this.repository.findByEmail(request.email());

        if(userResponse.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new RegisterErrorDTO(HttpStatus.BAD_REQUEST, 400, "Usuário já existe."));


        return ResponseEntity.status(HttpStatus.CREATED).body(repository.createUser(new UserModel(request)));
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        Optional<UserModel> userOptional = repository.findByEmail(request.email());

        if(userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        UserModel userData = userOptional.get();
        String token = tokenService.generateToken(userData);

        if(userData.getEmail().equals(request.email()) && passwordEncoder.matches(request.senha(), userData.getPassword()))
            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponseDTO(token, true));


        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    @RequestMapping("/obter")
    public ResponseEntity<List<GetAllUserResponseDTO>> pegarTodosOsUsuarios(){

        List<GetAllUserResponseDTO> todosOsUsuarios = repository.pegarTodosOsUsuarios();

        return ResponseEntity.ok(todosOsUsuarios);
    }

    @GetMapping
    @RequestMapping("/obter/{id}")
    public ResponseEntity pegarPeloId(@PathVariable Integer id){
        Optional<UserModel> pegarPeloId = repository.findById(id);

        if(pegarPeloId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        return ResponseEntity.ok(pegarPeloId);
    }

    @PutMapping
    @RequestMapping("/editar/{email}")
    public ResponseEntity editarSenha(@PathVariable String email, @RequestBody UserEditRequestDTO request){
        Optional<UserModel> userResponse = this.repository.findByEmail(email);

        if(userResponse.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        String senha = passwordEncoder.encode(request.senha());

        userResponse.get().setSenha(senha);
        BeanUtils.copyProperties(request, userResponse);

        repository.atualizarUsuario(userResponse.get());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    @RequestMapping("/editar/primeiro-acesso")
    public ResponseEntity editarSenha(@RequestBody FirstAcessRequestDTO data){
        String senhaCriptografada = passwordEncoder.encode(data.senha());
        UserModel usuarioPrimeiroAcesso = repository.primeiroAcesso(data, senhaCriptografada);

        System.out.print(usuarioPrimeiroAcesso);

        if(usuarioPrimeiroAcesso != null)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new TokenResponseDTO(tokenService.generateToken(usuarioPrimeiroAcesso), true));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RegisterErrorDTO(HttpStatus.BAD_REQUEST, 400, "Informações Inválidas ou Primeiro acesso já feito."));
    }
}