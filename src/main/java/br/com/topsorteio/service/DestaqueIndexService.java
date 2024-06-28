package br.com.topsorteio.service;

import br.com.topsorteio.dtos.DestaqueIndexRequestDTO;
import br.com.topsorteio.dtos.DestaqueIndexResponseDTO;
import br.com.topsorteio.entities.DestaqueIndexModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventBadRequestException;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.repositories.IDestaqueIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DestaqueIndexService {

    @Autowired
    private IDestaqueIndexRepository repository;

    @Autowired
    private UserService userService;

    public ResponseEntity obterDestaqueIndex() {
        try {
            List<DestaqueIndexModel> destaqueIndex = repository.findAll();
            List<DestaqueIndexResponseDTO> responseDTO = new ArrayList<>();

                for (DestaqueIndexModel destaque : destaqueIndex) {
                    DestaqueIndexResponseDTO dto = new DestaqueIndexResponseDTO(
                            destaque.getId(),
                            destaque.getNome(),
                            destaque.getTitulo(),
                            destaque.getImagem(),
                            destaque.getCriadoPor().getNome(),
                            destaque.getCriadoEm()
                    );
                    responseDTO.add(dto);
                }

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }catch (Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity obterDestaqueIndexPorId(Long id){
        try{
            DestaqueIndexModel destaqueIndex = repository.findById(id).orElseThrow(() -> new EventNotFoundException("Id do destaque index: " + id + "não encontrado" ));

            DestaqueIndexResponseDTO responseDTO = new DestaqueIndexResponseDTO(
                    destaqueIndex.getId(),
                    destaqueIndex.getNome(),
                    destaqueIndex.getTitulo(),
                    destaqueIndex.getImagem(),
                    destaqueIndex.getCriadoPor().getNome(),
                    destaqueIndex.getCriadoEm());

            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public void limiteDestaqueIndex(){
        int destaqueLimite = repository.findAll().size();
        if(destaqueLimite >= 3){
            throw new EventBadRequestException("Limite de 3 destaque atingindo");
        }
    }

    public ResponseEntity adicionarDestaqueIndex(DestaqueIndexRequestDTO requestDTO){
        try{
            limiteDestaqueIndex();
            DestaqueIndexModel destaqueIndex = new DestaqueIndexModel(requestDTO);
            UserModel user = userService.getAuthenticatedUser();
            destaqueIndex.setCriadoPor(user);
            repository.save(destaqueIndex);
            return new ResponseEntity("Destaque Index registrada com sucesso. ", HttpStatus.CREATED);
        }catch (IOException e){
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }

    public ResponseEntity editarDestaqueIndex(Long id, DestaqueIndexRequestDTO request){
        try{
            DestaqueIndexModel destaqueIndex = repository.findById(id).orElseThrow(() -> new EventNotFoundException("Destaque index com o ID " + id + " não encontrada."));

            destaqueIndex.setNome(request.nome());
            destaqueIndex.setTitulo(request.titulo());
            destaqueIndex.setImagem(request.imagem().getBytes());

            repository.save(destaqueIndex);

            return new ResponseEntity("Destaque editado com sucesso", HttpStatus.OK);
        } catch (IOException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }

    public ResponseEntity removerDestaqueIndex(Long id){
        try{
            repository.findById(id).orElseThrow(() -> new EventNotFoundException("Destaque com ID " + id + " não encontrada."));
            repository.deleteById(id);
            return new ResponseEntity("Destaque index removida com sucesso. ", HttpStatus.OK);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }




}

