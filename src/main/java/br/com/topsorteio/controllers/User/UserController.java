package br.com.topsorteio.controllers.User;

import br.com.topsorteio.dtos.userdto.UserResponseDTO;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private iUserRepository repository;

    @GetMapping
    @RequestMapping("/usuarios")
    public ResponseEntity<List<UserResponseDTO>> pegarTodosOsUsuarios(){
        List<UserModel> allUser = repository.findAll();
        List<UserResponseDTO> response = new ArrayList<>();

        for(UserModel user : allUser){
            UserResponseDTO responseDTO = new UserResponseDTO(user.getNome(), user.getEmail());
            response.add(responseDTO);
        }

        return ResponseEntity.ok(response);
    }
}
