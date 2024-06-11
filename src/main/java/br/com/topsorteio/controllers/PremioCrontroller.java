package br.com.topsorteio.controllers;

import java.util.List;

import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	
	@PostMapping ("/registrar")
	public ResponseEntity<?> registrarPremio(@RequestBody PremioRegisterRequestDTO request){
        return repository.registrarPremio(request);
    }

    @GetMapping("/obter")
    public ResponseEntity<List<GetAllPremiosResponseDTO>> obterTodosOsPremios(){
        return repository.obterTodosOsPremios();
    }

    @GetMapping("/obter/{codigoSku}")
    public ResponseEntity <?> obterPremioporCodigoSku (@PathVariable String codigoSku){
        return repository.buscarPorCodigoSku(codigoSku);
    }

    @PutMapping("/editar/{codigoSku}")
    public ResponseEntity<?> editarPremio(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
        try {
            ResponseEntity<?> response = repository.editarPremio(codigoSku, request);
            return response;
        } catch (RuntimeException ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }
    @DeleteMapping("/remover")
    public ResponseEntity<?> removerPremio(@PathVariable String codigoSku, @RequestBody PremioEditRequestDTO request) {
    	return repository.removerPremio(codigoSku);
    	
    }
}








