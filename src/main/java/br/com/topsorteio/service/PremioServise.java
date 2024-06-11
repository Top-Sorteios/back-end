package br.com.topsorteio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import br.com.topsorteio.dtos.ErrorDTO;
import br.com.topsorteio.dtos.GetAllPremiosResponseDTO;
import br.com.topsorteio.dtos.PremioEditRequestDTO;
import br.com.topsorteio.dtos.PremioRegisterRequestDTO;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.repositories.IMarcaRepository;
import br.com.topsorteio.repositories.IPremioRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class PremioService {

    @Autowired
    private IPremioRepository repository;

    @Autowired
    private IMarcaRepository marcaRepository;



    public ResponseEntity<Optional<PremioModel>> buscarPorCodigoSku(String codigoSku) {
        try {
            Optional<PremioModel> premio = repository.findByCodigoSku(codigoSku);
            return ResponseEntity.ok(premio); // Retorna um ResponseEntity com o resultado da busca
        } catch (RuntimeException ex) {
            throw new EventInternalServerErrorException(ex.getMessage()); // Propaga a exceção para ser tratada em outro lugar
        }
    }

    public ResponseEntity<List<GetAllPremiosResponseDTO>> obterTodosOsPremios() {
        List<PremioModel> premios = repository.findAll();
        List<GetAllPremiosResponseDTO> response = new ArrayList<>();

        if (premios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        for (PremioModel premio : premios) {
            GetAllPremiosResponseDTO premioDTO = new GetAllPremiosResponseDTO(
                    premio.getCodigoSku(),
                    premio.getNome(),
                    premio.getQuantidade(),
                    premio.getDescricao(),
                    premio.getImagem(),
                    premio.getMarca());
            response.add(premioDTO);
        } return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // Método para pesquisar prêmios pelo nome
    public ResponseEntity<List<PremioModel>> pesquisarPremiosPorNome(String nome) {
        try {
            List<PremioModel> premios = repository.findByNomeContainingIgnoreCase(nome);
            if (premios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(premios, HttpStatus.OK);
        } catch (RuntimeException ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }
    public ResponseEntity<?> registrarPremio(PremioRegisterRequestDTO request) {
        try {

            MarcaModel marcaModel = marcaRepository.findByNome(request.marca().getNome())
                    .orElseGet(() -> {
                        MarcaModel newMarca = new MarcaModel();
                        newMarca.setNome(request.marca().getNome());
                        return marcaRepository.save(newMarca);
                    });

            Optional<PremioModel> premioExistente = repository.findByCodigoSku(request.codigoSku());
            if (premioExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Prêmio já existe.", false));
            }

            PremioModel premioModel = new PremioModel(request.codigoSku(), request.nome(), request.quantidade(), request.descricao(),
                    request.imagem(), marcaModel, request.criadoPor());
            premioModel = repository.save(premioModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(premioModel);
        } catch (Exception e) { throw new RuntimeException(e);}
    }

    public ResponseEntity<?> editarPremio(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
        try {
            Optional<PremioModel> premioOpt = repository.findByCodigoSku(codigoSku);

            if (premioOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            PremioModel premio = premioOpt.get();

            premio.setNome(request.nome() != null ? request.nome() : premio.getNome());
            premio.setDescricao(request.descricao() != null ? request.descricao() : premio.getDescricao());
            premio.setImagem(request.imagem() != null && request.imagem().length > 0 ? request.imagem() : premio.getImagem());
            premio.setQuantidade(request.quantidade() > 0 ? request.quantidade() : premio.getQuantidade());

            repository.save(premio);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity<?> removerPremio(@PathVariable String codigoSku) {
        Optional<PremioModel> premioOpt = repository.findByCodigoSku(codigoSku);

        if (premioOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna um ResponseEntity com status NOT_FOUND se o prêmio não for encontrado
        }

        repository.delete(premioOpt.get()); // Deleta o prêmio encontrado

        return ResponseEntity.noContent().build(); // Retorna um ResponseEntity com status NO_CONTENT
    }
}

