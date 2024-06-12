package br.com.topsorteio.service;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.TurmaModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.entities.UserRole;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
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


    public ResponseEntity<Object> login(LoginRequestDTO data) {
        try {

            Optional<UserModel> userOptional = repository.findByEmail(data.email());
            if (userOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserModel userData = userOptional.get();

            if (userData.getSenha() == null)
                return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Realize o primeiro acesso.", false), HttpStatus.BAD_REQUEST);


            if (userData.getEmail().equals(data.email()) && passwordEncoder.matches(data.senha(), userData.getPassword()))
                return new ResponseEntity<>(new TokenResponseDTO(userData.getEmail(), tokenService.generateToken(userData), true), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (RuntimeException ex) {
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

    public ResponseEntity<UserResponseDTO> acharPeloEmail(String email){
        try{

            Optional<UserModel> pegarPeloEmail = repository.findByEmail(email);
            if(pegarPeloEmail.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserModel usuario = pegarPeloEmail.get();
            return new ResponseEntity<>(new UserResponseDTO(usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.getAdm(), usuario.getStatus(), usuario.getDataNascimento(), usuario.getTurma()), HttpStatus.OK);

        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }

//    ------------
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
                    UserModel usuario = new UserModel();
                    Optional<UserModel> criador = repository.findByEmail(request.email());


                    if(criador.isEmpty()) return new ResponseEntity(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Criador Inválido", false), HttpStatus.BAD_REQUEST);

                    usuario.setNome(getCellStringValue(row, 5));

                    String Turma = (String) getCellStringValue(row, 10);
                    Optional<TurmaModel> turma = turmaRepository.findByNome(Turma);

                    if(turma.isEmpty()) return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Não consta Turma", false), HttpStatus.BAD_REQUEST);

                    usuario.setTurma(turma.get());
                    usuario.setStatus(getCellStringValue(row, 11));
                    usuario.setCpf(getCellStringValue(row, 15));
                    usuario.setEmail(getCellStringValue(row, 30));
                    usuario.setCriadoPor(criador.get().getId());

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

                        if(convertToUserRole(userRoleInt) == UserRole.ADMIN){
                            usuario.setAdm(UserRole.ADMIN);
                        }else {
                            usuario.setAdm(UserRole.USER);
                        }
                    }catch(Exception ex){
                        usuario.setAdm(UserRole.USER);
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
                        updateUsuario.setAdm(usuario.getAdm());
                        novoUsuario.add(updateUsuario);
                    } else {
                        novoUsuario.add(usuario);
                    }
                }
            }
            repository.saveAll(novoUsuario);
            ImportUsuarioResponseDTO response = new ImportUsuarioResponseDTO(HttpStatus.OK, "Usuários importados com sucesso!");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        } catch (RuntimeException ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    private String getCellStringValue(Row row, int cellIndex) {
        return row.getCell(cellIndex).getCellType() == CellType.STRING ? row.getCell(cellIndex).getStringCellValue() : "";
    }
    private double getCellNumericValue(Row row, int cellIndex) {
        return row.getCell(cellIndex).getCellType() == CellType.NUMERIC ? row.getCell(cellIndex).getNumericCellValue() : 0;
    }
    private UserRole convertToUserRole(int value) {
        UserRole role;
        switch(value) {
            case 0:
                role = UserRole.USER;
                break;
            case 1:
                role = UserRole.ADMIN;
                break;
            default:
                role = UserRole.USER;
        }

        return role;
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
//------------------


//    --------------
    public ResponseEntity primeiroAcesso(FirstAcessRequestDTO data){
        try{
            Optional<UserModel> usuario = repository.findByEmail(data.email());

            if(usuario.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserModel usuarioPrimeiroAcesso = verificarPrimeiroAcesso(data);

            if(usuarioPrimeiroAcesso != null)
                return new ResponseEntity<>(new TokenResponseDTO(data.email(), tokenService.generateToken(usuarioPrimeiroAcesso), true), HttpStatus.OK);

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
    public ResponseEntity<List<UserResponseDTO>> pegarTodosOsUsuarios(){
        try{
            List<UserModel> allUser = repository.findAll();
            List<UserResponseDTO> response = new ArrayList<>();

            if(allUser.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


            for(UserModel user : allUser)
                response.add(new UserResponseDTO(user.getNome(), user.getEmail(), user.getCpf(), user.getAdm(), user.getStatus(), user.getDataNascimento(), user.getTurma()));

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
