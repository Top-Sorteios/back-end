package br.com.topsorteio.service;

import br.com.topsorteio.dtos.ErrorDTO;
import br.com.topsorteio.dtos.GetAllPremiosResponseDTO;
import br.com.topsorteio.dtos.PremioEditRequestDTO;
import br.com.topsorteio.dtos.PremioRegisterRequestDTO;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.repositories.IMarcaRepository;
import br.com.topsorteio.repositories.IPremioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PremioService {

    @Autowired
    private IPremioRepository premioRepository;

    @Autowired
    private IMarcaRepository marcaRepository;

    public ResponseEntity<List<GetAllPremiosResponseDTO>> obterTodosOsPremios() {
        List<PremioModel> premios = premioRepository.findAll();
        List<GetAllPremiosResponseDTO> response = new ArrayList<>();

        if(premios.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        for(PremioModel premio : premios)
            response.add(new GetAllPremiosResponseDTO(premio.getNome(), premio.getCodigoSku(), premio.getImagem(), premio.getQuantidade(), premio.getDescricao(), premio.getMarca().getId(), premio.getMarca().getNome()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Optional<PremioModel>> obterPremioPorId(Integer id) {
        Optional<PremioModel> premio = premioRepository.findById(id);
        if(premio.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(premio, HttpStatus.OK);
    }

    public ResponseEntity<?> registrarPremio(PremioRegisterRequestDTO request) {
        Optional<PremioModel> premio = premioRepository.findByCodigoSku(request.codigoSku());
        if(premio.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Código SKU já existe.", false));
        }

        Optional<MarcaModel> marca = marcaRepository.findById(request.marcaId());
        if (marca.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorDTO(HttpStatus.NOT_FOUND, 404, "Marca não encontrada.", false));
        }

        PremioModel premioSalvo = new PremioModel(request, marca.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(premioRepository.save(premioSalvo));
    }

    public ResponseEntity<?> editarPremio(@PathVariable Integer id, @RequestBody PremioEditRequestDTO request){
            Optional<PremioModel> premio = premioRepository.findById(id);
            if (premio.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            PremioModel premioSalvo = premio.get();

            Optional<PremioModel> existingSkuPremio = premioRepository.findByCodigoSku(request.codigoSku());
            if (existingSkuPremio.isPresent() && !existingSkuPremio.get().getId().equals(premioSalvo.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Código SKU já existe.", false));
            }

            if (request.marcaId() != null) {
                Optional<MarcaModel> marcaOptional = marcaRepository.findById(request.marcaId());
                if (marcaOptional.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                premioSalvo.setMarca(marcaOptional.get());
            }

            premioSalvo.setNome(request.nome());
            premioSalvo.setCodigoSku(request.codigoSku());
            premioSalvo.setImagem(request.imagem());
            premioSalvo.setQuantidade(request.quantidade());
            premioSalvo.setDescricao(request.descricao());

            premioRepository.save(premioSalvo);
            return new ResponseEntity<>(premioSalvo, HttpStatus.OK);
    }


    public ResponseEntity<?> removerPremio(Integer id) {
        Optional<PremioModel> premio = premioRepository.findById(id);
        if(premio.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        premioRepository.deleteById(id);
        return new ResponseEntity<>("Prêmio de ID " + id + " removida com sucesso.", HttpStatus.OK);
    }
}
