package br.com.topsorteio.service;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.infra.email.EmailService;
import br.com.topsorteio.infra.security.TokenService;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private iUserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

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
    public Optional<UserModel> findByEmail(String email){return repository.findByEmail(email);}

    public ResponseEntity<Optional<UserModel>> acharPeloID(Integer id){
        try{

            Optional<UserModel> pegarPeloId = repository.findById(id);
            if(pegarPeloId.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(pegarPeloId, HttpStatus.OK);

        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }
    public ResponseEntity registrarUsuario(UserRegisterRequestDTO data) {
        Optional<UserModel> userResponse = this.repository.findByEmail(data.email());

        if(userResponse.isPresent())
            return new ResponseEntity<>(new ErrorDTO(HttpStatus.CONFLICT, 400, "Usuário já existe.", false), HttpStatus.CONFLICT);


        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(new UserModel(data)));
    }

    public ResponseEntity editarSenha(UserEditRequestDTO data, String email){
        try{
            Optional<UserModel> userResponse = this.repository.findByEmail(email);
            if(userResponse.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


            if(!(passwordEncoder.matches(data.senhaAtual(), userResponse.get().getSenha()) && email.equals(userResponse.get().getEmail())))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            userResponse.get().setSenha(passwordEncoder.encode(data.senha()));
            BeanUtils.copyProperties(data, userResponse);

            repository.save(userResponse.get());

            return new ResponseEntity<>(HttpStatus.OK);

        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }


    }

//    --------------
    public ResponseEntity primeiroAcesso(FirstAcessRequestDTO data){
        try{
            UserModel usuarioPrimeiroAcesso = verificarPrimeiroAcesso(data);

            if(usuarioPrimeiroAcesso != null)
                return new ResponseEntity<>(new TokenResponseDTO(tokenService.generateToken(usuarioPrimeiroAcesso), true), HttpStatus.OK);

            return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Informações Inválidas ou Primeiro acesso já feito.", false), HttpStatus.BAD_REQUEST);

        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }
    private UserModel verificarPrimeiroAcesso(FirstAcessRequestDTO data){
        try{
            Optional<UserModel> usuario = this.repository.findByEmail(data.email());

            if(usuario.isEmpty()) return null;

            UserModel usuarioModel = usuario.get();

            if(usuarioModel.getEmail().equals(data.email())
                    && usuarioModel.getCpf().equals(data.cpf())
                    && usuarioModel.getDataNascimento().equals(data.datanascimento())
                    && (usuarioModel.getSenha() == null)){

                BeanUtils.copyProperties(data, usuario.get());
                usuarioModel.setSenha(passwordEncoder.encode(data.senha()));

                this.repository.save(usuario.get());

                return usuarioModel;
            }

            return null;
        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }
//    ---------------
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

    public ResponseEntity esqueciSenha(EsqueciSenhaRequestDTO data){
        try{
            Optional<UserModel> usuario = repository.findByEmail(data.email());

            if(usuario.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UUID geradorDeSenha = UUID.randomUUID();
            String[] senha = geradorDeSenha.toString().split("-");

            emailService.sendEmail(new EmailSenderDTO(data.email(), "Seila", "Sua senha foi alterada: " + senha[0]));
            String senhaEncriptografada = passwordEncoder.encode(senha[0]);

            usuario.get().setSenha(senhaEncriptografada);

            repository.save(usuario.get());

            return new ResponseEntity<>(new EsqueciSenhaResponseDTO(true), HttpStatus.OK);

        }catch(Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }




}
