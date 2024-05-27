package br.com.topsorteio.service;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.infra.security.TokenService;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private iUserRepository repository;

    @Autowired
    private TokenService tokenService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public ResponseEntity<Object> login(LoginRequestDTO data){
        try{

            Optional<UserModel> userOptional = repository.findByEmail(data.email());
            if(userOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserModel userData = userOptional.get();

            if(userData.getSenha() == null)
                return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Realize o primeiro acesso.", false), HttpStatus.BAD_REQUEST);


            String token = tokenService.generateToken(userData);


            if(userData.getEmail().equals(data.email()) && passwordEncoder.matches(data.senha(), userData.getPassword()))
                return new ResponseEntity<>(new TokenResponseDTO(token, true), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch(RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity<HttpStatus> editarSenha(String email, UserEditRequestDTO request){
        try{
            Optional<UserModel> userResponse = this.repository.findByEmail(email);

            if(userResponse.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


            userResponse.get().setSenha(passwordEncoder.encode(request.senha()));
            BeanUtils.copyProperties(request, userResponse);

            repository.save(userResponse.get());

            return new ResponseEntity<>(HttpStatus.OK);

        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public Optional<UserModel> findByEmail(String email){
        try{
            return repository.findByEmail(email);
        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public ResponseEntity<Optional<UserModel>> acharPeloID(Integer id){
        try{

            Optional<UserModel> pegarPeloId = repository.findById(id);
            if(pegarPeloId.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(pegarPeloId, HttpStatus.OK);

        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public UserModel createUser(UserModel user) {return repository.save(user);}

    public ResponseEntity editarSenha(FirstAcessRequestDTO data){
        String senhaCriptografada = passwordEncoder.encode(data.senha());
        UserModel usuarioPrimeiroAcesso = primeiroAcesso(data, senhaCriptografada);


        if(usuarioPrimeiroAcesso != null)
            return new ResponseEntity<>(new TokenResponseDTO(tokenService.generateToken(usuarioPrimeiroAcesso), true), HttpStatus.OK);


        return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Informações Inválidas ou Primeiro acesso já feito.", false), HttpStatus.BAD_REQUEST);
    }

    private UserModel primeiroAcesso(FirstAcessRequestDTO data, String senhaCriptografada){
        try{
            Optional<UserModel> usuario = this.repository.findByEmail(data.email());

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

    public UserModel atualizarUsuario(UserModel user){
        try{
            return repository.save(user);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity<List<GetAllUserResponseDTO>> pegarTodosOsUsuarios(){
        try{
            List<UserModel> allUser = repository.findAll();
            List<GetAllUserResponseDTO> response = new ArrayList<>();

            if(allUser.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


            for(UserModel user : allUser)
                response.add(new GetAllUserResponseDTO(user.getNome(), user.getEmail(), user.getAdm(), user.getStatus(), user.getDataNascimento(), user.getTurma()));

            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }




}
