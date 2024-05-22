package br.com.topsorteio.controllers.User;

import br.com.topsorteio.dtos.userdto.UserResponseDTO;
import br.com.topsorteio.entities.user.UserModel;
import br.com.topsorteio.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UserController {
    @Autowired
    private UserService repository;

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
    @RequestMapping("/{id}")
    public ResponseEntity pegarPeloId(@PathVariable Integer id){
        Optional<UserModel> pegarPeloId = repository.findById(id);

        if(pegarPeloId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        return ResponseEntity.ok(pegarPeloId);
    }
}