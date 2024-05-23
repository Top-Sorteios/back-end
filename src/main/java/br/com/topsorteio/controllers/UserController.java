package br.com.topsorteio.controllers;

import br.com.topsorteio.dtos.LoginRequestDTO;
import br.com.topsorteio.dtos.LoginResponseDTO;
import br.com.topsorteio.dtos.UserRegisterRequestDTO;
import br.com.topsorteio.dtos.UserResponseDTO;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventBadRequestException;
import br.com.topsorteio.exceptions.EventNotFoundException;
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
@RequestMapping("/usuario")
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

        if(userResponse.isPresent()) throw new EventBadRequestException("Usuário já existe.");

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

        throw new EventNotFoundException("Email ou Senha inválidos.");
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
}