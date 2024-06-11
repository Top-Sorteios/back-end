package br.com.topsorteio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.topsorteio.dtos.GetAllPremiosResponseDTO;
import br.com.topsorteio.dtos.PremioEditRequestDTO;
import br.com.topsorteio.dtos.PremioRegisterRequestDTO;
import br.com.topsorteio.service.PremioService;


@RestController
@CrossOrigin("*")
@RequestMapping("/premios")
public class PremioCrontroller {
	
	@Autowired
    private PremioService repository;
	
	@PostMapping
    @RequestMapping("/registrar")
	public ResponseEntity registrarPremio(@RequestBody PremioRegisterRequestDTO request){
        return repository.registrarPremio(request);
    }

	@PutMapping("/editar/nome/{codigoSku}")
    public ResponseEntity<?> editarPremioNome(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
        return repository.editarNome(request, codigoSku);
    }

    // Métodos adicionais para editar outros campos
    @PutMapping("/editar/descricao/{codigoSku}")
    public ResponseEntity<?> editarPremioDescricao(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
        return repository.editarDescricao(codigoSku, request);
    }

    @PutMapping("/editar/quantidade/{codigoSku}")
    public ResponseEntity<?> editarPremioQuantidade(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
        return repository.editarQuantidade(codigoSku, request);
    }

    @PutMapping("/editar/imagem/{codigoSku}")
    public ResponseEntity<?> editarPremioImagem(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
        return repository.editarImagem(codigoSku, request);
    }
    
    @PutMapping("/remover")
    public ResponseEntity<?> removerPremio(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
    	return repository.removerPremio(codigoSku);
    	
    }
    
    @PutMapping("/obter")
    public ResponseEntity<List<GetAllPremiosResponseDTO>> obterTodosOsPremios(){
    	return repository.obterTodosOsPremios();
    }
}








