package br.com.topsorteio.service;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.TurmaModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventBadRequestException;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.infra.email.EmailService;
import br.com.topsorteio.infra.security.TokenService;
import br.com.topsorteio.repositories.iTurmaRepository;
import br.com.topsorteio.repositories.iUserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private iUserRepository repository;

    @Autowired
    private iTurmaRepository turmaRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private UserModel validateEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new EventNotFoundException("Usuário não encontrado."));
    }

    public UserModel getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserModel) authentication.getPrincipal();
    }

    public ResponseEntity<Object> login(LoginRequestDTO data) {
        try {

            Optional<UserModel> userOptional = repository.findByEmail(data.email());

            if (userOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserModel userData = userOptional.get();

            if (userData.getSenha() == null || userData.getSenha().isEmpty())
                return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Realize o primeiro acesso.", false), HttpStatus.BAD_REQUEST);

            if (userData.getEmail().equals(data.email()) && passwordEncoder.matches(data.senha(), userData.getPassword()))
                return new ResponseEntity<>(new TokenResponseDTO(userData.getEmail(), tokenService.generateToken(userData), true), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException ex) {
            throw new EventBadRequestException(ex.getMessage());
        }
    }

    public Optional<UserModel> findByEmail(String email){return repository.findByEmail(email);}

    public ResponseEntity<UserResponseDTO> acharPeloEmail(String email){
        try{

            UserModel usuario = validateEmail(email);

            return new ResponseEntity<>(new UserResponseDTO(usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.isAdm(), usuario.getStatus(), usuario.getDataNascimento(), new TurmaResponseDTO(usuario.getTurma().getId(), usuario.getTurma().getNome(), usuario.getTurma().isParticipandoSorteio(), usuario.getTurma().getCriadoem())), HttpStatus.OK);

        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }


    public ResponseEntity cadastrarUsuario(ImportUsuarioRequestDTO request) {
        MultipartFile file = request.file();
        if (file == null || file.isEmpty()) {
            throw new EventInternalServerErrorException("Arquivo não fornecido ou vazio.");
        }

        try {
            List<UserModel> novoUsuario = new ArrayList<>();

            try (InputStream is = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) { // Ignorar a linha de cabeçalho
                        continue;
                    }
                    UserModel criador = validateEmail(request.email());

                    if(!criador.isAdm()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

                    UserModel usuario = new UserModel();

                    String Turma = (String) getCellStringValue(row, 10);
                    String Nome = (String) getCellStringValue(row, 5);
                    String Status = (String) getCellStringValue(row, 11).toLowerCase();
                    String Email = (String) getCellStringValue(row, 30);
                    String CPF = (String) getCpfStringValue(row, 15);


                    Optional<TurmaModel> turmaDoBanco = turmaRepository.findByNome(Turma);

                    //SetTurma
                    if(turmaDoBanco.isEmpty()){
                        TurmaModel criarTurma = new TurmaModel(Turma, true);
                        turmaRepository.save(criarTurma);
                        usuario.setTurma(criarTurma);
                    }else{
                        usuario.setTurma(turmaDoBanco.get());
                    }

                    usuario.setNome(Nome);
                    usuario.setStatus(Status);
                    usuario.setEmail(Email);
                    usuario.setCpf(CPF);
                    usuario.setCriadoPor(criador);

                    Cell dataNascimentoCell = row.getCell(14);
                    if (dataNascimentoCell != null) {
                        if (dataNascimentoCell.getCellType() == CellType.STRING) {
                            String dataNascimento = dataNascimentoCell.getStringCellValue();
                            if (!dataNascimento.isEmpty()) {
                                usuario.setDataNascimento(dataNascimento);
                            } else {
                                usuario.setDataNascimento(null);
                            }
                        } else if (dataNascimentoCell.getCellType() == CellType.NUMERIC) {
                            Date date = dataNascimentoCell.getDateCellValue();
                            usuario.setDataNascimento(new SimpleDateFormat("yyyy/MM/dd").format(date));
                        }
                    }

                    try{
                        int userRoleInt = (int) getCellNumericValue(row, 77);
                        usuario.setAdm(userRoleInt == 1);
                    }catch(Exception ex){
                        usuario.setAdm(false);
                    }

                    usuario.setParticipandoSorteio(false);


                    Optional<UserModel> updateUser = repository.findByEmail(usuario.getEmail());
                    if (updateUser.isPresent()) {
                        UserModel updateUsuario = updateUser.get();
                        updateUsuario.setTurma(usuario.getTurma());
                        updateUsuario.setNome(usuario.getNome());
                        updateUsuario.setStatus(usuario.getStatus());
                        updateUsuario.setCpf(usuario.getCpf());
                        updateUsuario.setEmail(usuario.getEmail());
                        updateUsuario.setDataNascimento(usuario.getDataNascimento());
                        updateUsuario.setAdm(usuario.isAdm());
                        novoUsuario.add(updateUsuario);
                    } else {
                        novoUsuario.add(usuario);
                    }
                }
            }
            repository.saveAll(novoUsuario);
            return new ResponseEntity<>(new ImportUsuarioResponseDTO(HttpStatus.CREATED, "Usuários importados com sucesso!"), HttpStatus.CREATED);
        }catch (IOException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }catch (RuntimeException ex) {
            throw new EventBadRequestException(ex.getMessage());
        }
    }
    private String getCellStringValue(Row row, int cellIndex) {
        return row.getCell(cellIndex).getCellType() == CellType.STRING ? row.getCell(cellIndex).getStringCellValue() : "";
    }
    private String getCpfStringValue(Row row, int cellIndex) {
        long cpf = (long) getCellNumericValue(row, cellIndex);

        return Long.toString(cpf);
    }
    private double getCellNumericValue(Row row, int cellIndex) {
        return row.getCell(cellIndex).getCellType() == CellType.NUMERIC ? row.getCell(cellIndex).getNumericCellValue() : 0;
    }


    public ResponseEntity editarSenha(UserEditPasswordRequestDTO data, String email){
        try{
            UserModel userResponse = validateEmail(email);

            if(!(passwordEncoder.matches(data.senhaAtual(), userResponse.getSenha()) && email.equals(userResponse.getEmail())))
                return new ResponseEntity<>("Senha Atual inválida", HttpStatus.BAD_REQUEST);

            userResponse.setSenha(passwordEncoder.encode(data.senha()));

            repository.save(userResponse);

            return new ResponseEntity<>(HttpStatus.OK);

        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }catch(RuntimeException ex){
            throw new EventBadRequestException();
        }


    }



    public ResponseEntity primeiroAcesso(FirstAcessRequestDTO data){
        try{
            UserModel usuarioPrimeiroAcesso = verificarPrimeiroAcesso(data);

            if(usuarioPrimeiroAcesso == null )
                return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Informações Inválidas ou Primeiro acesso já feito.", false), HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(new TokenResponseDTO(data.email(), tokenService.generateToken(usuarioPrimeiroAcesso), true), HttpStatus.OK);
        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }
    private UserModel verificarPrimeiroAcesso(FirstAcessRequestDTO data){
        try{
            UserModel usuario = validateEmail(data.email());

            if(usuario.getEmail().equals(data.email())
                    && usuario.getCpf().equals(data.cpf())
                    && usuario.getDataNascimento().equals(data.datanascimento())
                    && (usuario.getSenha() == null || usuario.getSenha().isEmpty())){

                BeanUtils.copyProperties(data, usuario);
                usuario.setSenha(passwordEncoder.encode(data.senha()));

                this.repository.save(usuario);

                return usuario;
            }

            return null;
        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }
//    ---------------
    public ResponseEntity<List<UserResponseDTO>> pegarTodosOsUsuarios(){
        try{
            List<UserModel> allUser = repository.findAll();
            List<UserResponseDTO> response = new ArrayList<>();

            if(allUser.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


            for(UserModel user : allUser)
                response.add(new UserResponseDTO(user.getNome(), user.getEmail(), user.getCpf(), user.isAdm(), user.getStatus(), user.getDataNascimento(), new TurmaResponseDTO(user.getTurma().getId(), user.getTurma().getNome(), user.getTurma().isParticipandoSorteio(), user.getTurma().getCriadoem())));

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public ResponseEntity esqueciSenha(EsqueciSenhaRequestDTO data) {
        try {
            UserModel usuario = validateEmail(data.email());

            UUID geradorDeSenha = UUID.randomUUID();
            String[] senha = geradorDeSenha.toString().split("-");

            emailService.sendEmail(new EmailSenderDTO(data.email(), "Nova Senha", "Sua senha foi alterada: " + senha[0] + "\n\nLogin: https://green-dune-0cd28a70f.5.azurestaticapps.net/html/login/index.html"));
            String senhaEncriptografada = passwordEncoder.encode(senha[0]);

            usuario.setSenha(senhaEncriptografada);

            repository.save(usuario);

            return new ResponseEntity<>(new EsqueciSenhaResponseDTO(true), HttpStatus.OK);

        } catch (Exception ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity participarSorteio(){
        try {
            UserModel user = getAuthenticatedUser();
            if (user.isParticipandoSorteio()) {
                throw new IllegalStateException("Usuário já está participando do sorteio.");
            }
            user.setParticipandoSorteio(true);
            repository.save(user);

            return new ResponseEntity<>("Usuário inscrito no sorteio com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }



    public ResponseEntity<?> editarPermissaoAdministrador(String email, UserEditTypeRequestDTO request) {
        try{
            Optional<UserModel> userOpt = repository.findByEmail(email);
            if(userOpt.isEmpty()){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

            UserModel user = userOpt.get();

            user.setAdm(request.adm());

            repository.save(user);

            return new ResponseEntity<>("Tipo do usuário alterado com sucesso", HttpStatus.OK);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }catch(RuntimeException ex){
            throw new EventBadRequestException(ex.getMessage());
        }
    }
}
