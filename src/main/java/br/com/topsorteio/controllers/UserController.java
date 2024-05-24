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

import java.util.ArrayList;
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
        UserModel user = new UserModel();

        if(userResponse.isPresent()) return ResponseEntity.status(HttpStatus.OK).body("Usuário já existe.");

        String password = passwordEncoder.encode(request.senha());

        BeanUtils.copyProperties(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.createUser(user));
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        Optional<UserModel> userResponse = this.repository.findByEmail(request.email());

        if(userResponse.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        var user = userResponse.get();

        String token = this.tokenService.generateToken(user);

        if(user.getEmail().equals(request.email()) && user.getSenha().equals(request.senha()))
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token, true));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
    }

    @GetMapping
    @RequestMapping("/obter")
    public ResponseEntity<List<UserResponseDTO>> pegarTodosOsUsuarios(){
        List<UserModel> allUser = repository.findAll();
        List<UserResponseDTO> response = new ArrayList<>();

        for(UserModel user : allUser){
            response.add(new UserResponseDTO(user.getNome(), user.getEmail(), user.getAdm(), user.getStatus()));
        }

        return ResponseEntity.ok(response);
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
    public ResponseEntity editarUsuario(@PathVariable String email, @RequestBody UserEditRequestDTO request){

        Optional<UserModel> userResponse = this.repository.findByEmail(email);

        if(userResponse.isEmpty())return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

        UserModel atributosUsuario = userResponse.get();
        String senha = passwordEncoder.encode(request.senha());
        atributosUsuario.setSenha(senha);
        BeanUtils.copyProperties(request, userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(repository.update(userResponse.get()));
    }
}