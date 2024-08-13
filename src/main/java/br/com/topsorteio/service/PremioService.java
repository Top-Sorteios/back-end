package br.com.topsorteio.service;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.repositories.IMarcaRepository;
import br.com.topsorteio.repositories.IPremioRepository;
import br.com.topsorteio.repositories.iTurmaRepository;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PremioService {

    @Autowired
    private IPremioRepository premioRepository;

    @Autowired
    private IMarcaRepository marcaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private iTurmaRepository turmaRepository;

    @Autowired
    private iUserRepository userRepository;

    public ResponseEntity<List<PremiosCadastradosResponseDTO>> obterTodosOsPremios() {
        try {
            List<PremioModel> premios = premioRepository.findAll();
            List<PremiosCadastradosResponseDTO> response = new ArrayList<>();

            if(premios.isEmpty()){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

            for(PremioModel premio : premios){
                response.add(new PremiosCadastradosResponseDTO(
                        premio.getId(),
                        premio.getNome(),
                        premio.getCodigoSku(),
                        premio.getDescricao(),
                        premio.getImagem(),
                        premio.getMarca().getNome(),
                        premio.getQuantidade(),
                        premio.getCriadoPor().getNome(),
                        premio.getCriadoEm()));
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }

   public ResponseEntity<List<PremiosCadastradosResponseDTO>> obterPremios(){
        try{
            List<PremioModel> premios = premioRepository.findAll();
            List<PremiosCadastradosResponseDTO> response = new ArrayList<>();

            if(premios.isEmpty()){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

            for(PremioModel premio : premios){
                if(premio.getQuantidade() >= 1) {
                    response.add(new PremiosCadastradosResponseDTO(
                            premio.getId(),
                            premio.getNome(),
                            premio.getCodigoSku(),
                            premio.getDescricao(),
                            premio.getImagem(),
                            premio.getMarca().getNome(),
                            premio.getQuantidade(),
                            premio.getCriadoPor().getNome(),
                            premio.getCriadoEm()));
                }
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
   }

    public ResponseEntity<PremioResponseDTO> obterPremioPorId(Integer id) {
        try {
            PremioModel premio = premioRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Prêmio com ID " + id + " não encontrado"));

            List<MarcaModel> opcoesMarcas = marcaRepository.findAll();
            List<OpcoesMarcasDTO> marcas = new ArrayList<>();

            for(MarcaModel marca : opcoesMarcas){
                marcas.add(new OpcoesMarcasDTO(marca.getId(), marca.getNome(), marca.getLogo()));
            }

            PremioResponseDTO response = new PremioResponseDTO(
                    premio.getId(),
                    premio.getNome(),
                    premio.getCodigoSku(),
                    premio.getDescricao(),
                    premio.getQuantidade(),
                    premio.getImagem(),
                    premio.getCriadoPor().getNome(),
                    premio.getCriadoEm(),
                    marcas
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JpaSystemException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }

    public ResponseEntity<?> registrarPremio(PremioRegisterRequestDTO request){
        try {
            Optional<PremioModel> premioOpt = premioRepository.findByCodigoSku(request.codigoSku());
            if (premioOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Código SKU já existe.", false));
            }

            MarcaModel marca = marcaRepository.findById(request.marcaId()).orElseThrow(() -> new EventNotFoundException("Marca com ID " + request.marcaId() + " não encontrada."));

            PremioModel premio = new PremioModel(request, marca);

            UserModel user = userService.getAuthenticatedUser();
            premio.setCriadoPor(user);
            premioRepository.save(premio);

            return new ResponseEntity<>("Prêmio registrado com sucesso.", HttpStatus.CREATED);
        } catch (IOException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }

    public ResponseEntity<?> editarPremio(Integer id, PremioEditRequestDTO request) {
        try {
            PremioModel premio = premioRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Prêmio de ID " + id + " não encontrado."));

            Optional<PremioModel> premioSkuExistente = premioRepository.findByCodigoSku(request.codigoSku());
            if (premioSkuExistente.isPresent() && !premioSkuExistente.get().getId().equals(premio.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Código SKU já existe.", false));
            }

            MarcaModel marca = marcaRepository.findById(request.marcaId()).orElseThrow(() -> new EventNotFoundException("Marca de ID " + request.marcaId() + " não encontrada."));

            premio.setNome(request.nome());
            premio.setCodigoSku(request.codigoSku());
            premio.setImagem(request.imagem().getBytes());
            premio.setQuantidade(request.quantidade());
            premio.setDescricao(request.descricao());
            premio.setMarca(marca);

            premioRepository.save(premio);
            return new ResponseEntity<>("Prêmio editado com sucesso", HttpStatus.OK);
        } catch (IOException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }

    public ResponseEntity<?> removerPremio(Integer id) {
        try {
            premioRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Prêmio com ID " + id + " não encontrado."));
            premioRepository.deleteById(id);
            return new ResponseEntity<>("Prêmio removido com sucesso.", HttpStatus.OK);
        } catch (JpaSystemException e) {
            throw new EventInternalServerErrorException(e.getMessage());
        }
    }
}