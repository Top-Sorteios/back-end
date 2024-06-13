//package br.com.topsorteio.service;
//
//import br.com.topsorteio.dtos.ErrorDTO;
//import br.com.topsorteio.dtos.PremioEditRequestDTO;
//import br.com.topsorteio.dtos.PremioRegisterRequestDTO;
//import br.com.topsorteio.entities.MarcaModel;
//import br.com.topsorteio.entities.PremioModel;
//import br.com.topsorteio.exceptions.EventInternalServerErrorException;
//import br.com.topsorteio.repositories.IMarcaRepository;
//import br.com.topsorteio.repositories.IPremioRepository;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.orm.jpa.JpaSystemException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class PremioService {
//
//    @Autowired
//    private IPremioRepository premioRepository;
//
//    @Autowired
//    private IMarcaRepository marcaRepository;
//
//
//
//    public ResponseEntity<Optional<PremioModel>> buscarPorCodigoSku(String codigoSku) {
//        try {
//            Optional<PremioModel> premio = premioRepository.findByCodigoSku(codigoSku);
//            // Verifica se o prêmio foi encontrado
//            if (premio.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 se o prêmio não for encontrado
//            }
//            return new ResponseEntity<>(premio, HttpStatus.OK); // Retorna o prêmio encontrado com status 200
//        } catch (JpaSystemException ex) {
//            throw new EventInternalServerErrorException("Erro ao buscar prêmio por SKU"); // Trata exceções do banco de dados
//        }
//    }
//
//    public ResponseEntity<Object> registrarPremio(PremioRegisterRequestDTO data) {
//        // Verifica se já existe um prêmio com o mesmo SKU
//        Optional<PremioModel> premioResponse = premioRepository.findByCodigoSku(data.getCodigoSku());
//
//        // Se o prêmio já existe, retorna um erro de conflito
//        if (premioResponse.isPresent()) {
//            return new ResponseEntity<>(new ErrorDTO(HttpStatus.CONFLICT, 400, "Prêmio já existe.", false), HttpStatus.CONFLICT);
//        }
//
//        try {
//            // Recupera a marca associada do banco de dados
//            MarcaModel marcaModel = marcaRepository.findByNome(data.getMarca())
//                    .orElseThrow(() -> new EventInternalServerErrorException("Marca parceira não encontrada."));
//
//            // Cria uma nova entidade de prêmio usando os dados do DTO
//            PremioModel premioModel = new PremioModel(
//                    data.getCodigoSku(),
//                    data.getNome(),
//                    data.getQuantidade(),
//                    data.getDescricao(),
//                    data.getImagem(),
//                    marcaModel // Associa o prêmio à marca
//            );
//
//            // Salva a nova entidade de prêmio no banco de dados
//            premioModel = premioRepository.save(premioModel);
//
//            // Retorna o prêmio criado com status 201
//            return new ResponseEntity<>(premioModel, HttpStatus.CREATED);
//        } catch (JpaSystemException ex) {
//            // Trata exceções do banco de dados
//            throw new EventInternalServerErrorException("Erro ao salvar prêmio");
//        }
//    }
//
//
//    public ResponseEntity<HttpStatus> editarNome(PremioEditRequestDTO request, String codigoSku) {
//        try {
//            Optional<PremioModel> premioOpt = premioRepository.findByCodigoSku(codigoSku);
//
//            if (premioOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//            PremioModel premio = premioOpt.get();
//            premio.setNome(request.nome());
//
//            premioRepository.save(premio);
//
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (RuntimeException ex) {
//            throw new EventInternalServerErrorException(ex.getMessage());
//        }
//    }
//
//    public ResponseEntity<HttpStatus> editarDescricao(String nome, PremioEditRequestDTO request) {
//        try {
//            Optional<PremioModel> premioOpt = premioRepository.findByNome(nome);
//
//            if (premioOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//            PremioModel premio = premioOpt.get();
//            premio.setDescricao(request.descricao());
//
//            premioRepository.save(premio);
//
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (RuntimeException ex) {
//            throw new EventInternalServerErrorException(ex.getMessage());
//        }
//    }
//
//
//    public ResponseEntity<HttpStatus> editarImagem(String codigoSku, PremioEditRequestDTO request) {
//        try {
//            Optional<PremioModel> premioOpt = premioRepository.findByCodigoSku(codigoSku);
//
//            if (premioOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//            PremioModel premio = premioOpt.get();
//
//            // Verifica se a nova imagem foi fornecida
//            if (request.imagem()!= null && request.imagem().length > 0) {
//                premio.setImagem(request.imagem());
//            } else {
//                // Retorna erro se a nova imagem estiver ausente
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            premioRepository.save(premio);
//
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (RuntimeException ex) {
//            throw new EventInternalServerErrorException(ex.getMessage());
//        }
//    }
//
//    public ResponseEntity<HttpStatus> editarQuantidade(String codigoSku, PremioEditRequestDTO request) {
//        try {
//            Optional<PremioModel> premioOpt = premioRepository.findByCodigoSku(codigoSku);
//
//            if (premioOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//            PremioModel premio = premioOpt.get();
//
//            // Verifica se a nova quantidade é válida (maior que zero)
//            if (request.quantidade() > 0) {
//                premio.setQuantidade(request.quantidade());
//            } else {
//                // Retorna erro se a nova quantidade for inválida
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            premioRepository.save(premio);
//
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (RuntimeException ex) {
//            throw new EventInternalServerErrorException(ex.getMessage());
//        }
//    }
//
//    public ResponseEntity<HttpStatus> removerPremio(String codigoSku) {
//        try {
//            Optional<PremioModel> premioOpt = premioRepository.findByCodigoSku(codigoSku);
//            if (premioOpt.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            premioRepository.delete(premioOpt.get());
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException ex) {
//            throw new EventInternalServerErrorException(ex.getMessage());
//        }
//    }
//
//    public ResponseEntity<List<PremioModel>> listarTodosPremios() {
//        try {
//            List<PremioModel> premios = premioRepository.findAll();
//            if (premios.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(premios, HttpStatus.OK);
//        } catch (RuntimeException ex) {
//            throw new EventInternalServerErrorException(ex.getMessage());
//        }
//    }
//
//    // Método para pesquisar prêmios pelo nome
//    public ResponseEntity<List<PremioModel>> pesquisarPremiosPorNome(String nome) {
//        try {
//            List<PremioModel> premios = premioRepository.findByNomeContainingIgnoreCase(nome);
//            if (premios.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(premios, HttpStatus.OK);
//        } catch (RuntimeException ex) {
//            throw new EventInternalServerErrorException(ex.getMessage());
//        }
//    }
//
//
//
//}
//
