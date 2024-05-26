package br.com.topsorteio.service;

import br.com.topsorteio.dtos.FirstAcessRequestDTO;
import br.com.topsorteio.dtos.GetAllUserResponseDTO;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.repositories.iUserRepository;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public Optional<UserModel> findByEmail(String email){
        try{
            return repository.findByEmail(email);
        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public Optional<UserModel> findById(Integer id){
        try{
            return repository.findById(id);
        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public UserModel createUser(UserModel user) {return repository.save(user);}

    public UserModel atualizarUsuario(UserModel user){
        try{
            return repository.save(user);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
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

    public UserModel primeiroAcesso(FirstAcessRequestDTO data, String senhaCriptografada){
        try{
            Optional<UserModel> usuario = this.repository.findByEmail(data.email());

            System.out.println(usuario);

            if(usuario.isEmpty()) return null;

            UserModel usuarioModel = usuario.get();

            if(usuarioModel.getEmail().equals(data.email())
                    && usuarioModel.getCpf().equals(data.cpf())
                    && usuarioModel.getDataNascimento().equals(data.datanascimento())
                    && (usuarioModel.getSenha() == null)){

                usuarioModel.setSenha(senhaCriptografada);
                BeanUtils.copyProperties(data, usuario.get());

                usuarioModel.setSenha(senhaCriptografada);
                this.repository.save(usuario.get());

                return usuarioModel;
            }

            return null;
        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }


}
