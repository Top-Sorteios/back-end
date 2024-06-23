package br.com.topsorteio.service;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.repositories.IMarcaRepository;
import br.com.topsorteio.repositories.IPremioRepository;
import br.com.topsorteio.repositories.iTurmaRepository;
import br.com.topsorteio.repositories.iUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private EntityManager entityManager;

    @Autowired
    private iTurmaRepository turmaRepository;

    @Autowired
    private iUserRepository userRepository;

    public ResponseEntity<List<PremiosCadastradosResponseDTO>> obterTodosOsPremios() {
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
                    premio.getCriadoPor().getNome(),
                    premio.getCriadoEm()));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<PremioResponseDTO> obterPremioPorId(Integer id) {
        Optional<PremioModel> premioOpt = premioRepository.findById(id);
        if(premioOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<MarcaModel> opcoesMarcas = marcaRepository.findAll();
        List<OpcoesMarcasDTO> marcas = new ArrayList<>();

        for(MarcaModel marca : opcoesMarcas){
            marcas.add(new OpcoesMarcasDTO(marca.getId(), marca.getNome(), marca.getLogo()));
        }

        PremioModel premio = premioOpt.get();
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
    }

    public ResponseEntity<?> registrarPremio(PremioRegisterRequestDTO request) throws IOException {
        Optional<PremioModel> premioOpt = premioRepository.findByCodigoSku(request.codigoSku());
        if (premioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Código SKU já existe.", false));
        }

        Optional<MarcaModel> marcaOpt = marcaRepository.findById(request.marcaId());
        if (marcaOpt.isEmpty()) {
            return new ResponseEntity<>("Marca não encontrada", HttpStatus.NOT_FOUND);
        }
        PremioModel premio = new PremioModel(request, marcaOpt.get());
        UserModel user = userService.getAuthenticatedUser();
        premio.setCriadoPor(user);
        premioRepository.save(premio);

        return new ResponseEntity<>("Prêmio registrado com sucesso.", HttpStatus.CREATED);
    }

    public ResponseEntity<?> editarPremio(Integer id, PremioEditRequestDTO request) throws IOException{
        Optional<PremioModel> premioOpt = premioRepository.findById(id);
        if (premioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PremioModel premio = premioOpt.get();

        Optional<PremioModel> premioSkuExistente = premioRepository.findByCodigoSku(request.codigoSku());
        if (premioSkuExistente.isPresent() && !premioSkuExistente.get().getId().equals(premio.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Código SKU já existe.", false));
        }

        Optional<MarcaModel> marcaOpt = marcaRepository.findById(request.marcaId());
        if (marcaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Marca não existe.", false));
        }

        premio.setNome(request.nome());
        premio.setCodigoSku(request.codigoSku());
        premio.setImagem(request.imagem().getBytes());
        premio.setQuantidade(request.quantidade());
        premio.setDescricao(request.descricao());
        premio.setMarca(marcaOpt.get());

        premioRepository.save(premio);
        return new ResponseEntity<>("Prêmio editado com sucesso", HttpStatus.OK);
    }

    public ResponseEntity<?> removerPremio(Integer id) {
        Optional<PremioModel> premio = premioRepository.findById(id);
        if(premio.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        premioRepository.deleteById(id);
        return new ResponseEntity<>("Prêmio removido com sucesso.", HttpStatus.OK);
    }
}