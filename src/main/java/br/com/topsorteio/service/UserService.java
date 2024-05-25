package br.com.topsorteio.service;

import br.com.topsorteio.dtos.GetAllUserResponseDTO;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class UserService {

    @Autowired
    private iUserRepository repository;


    public List<UserModel> findAll(){
        try{
            return repository.findAll();
        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public Optional<UserModel> findByEmail(String email){
        try{
            return repository.findByEmail(email);
        }catch(RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public Optional<UserModel> findById(Integer id){
        try{
            return repository.findById(id);
        }catch(RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public UserModel createUser(UserModel user) {return repository.save(user);}

    public UserModel updateUser(UserModel user){
        try{
            return repository.save(user);
        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public List<GetAllUserResponseDTO> pegarTodosOsUsuarios(){
        try{

            List<UserModel> allUser = repository.findAll();
            List<GetAllUserResponseDTO> response = new ArrayList<>();

            for(UserModel user : allUser)
                response.add(new GetAllUserResponseDTO(user.getNome(), user.getEmail(), user.getAdm(), user.getStatus()));

            return response;

        }catch(RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }


}
