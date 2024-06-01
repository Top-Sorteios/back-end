package br.com.topsorteio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.topsorteio.dtos.GetAllMarcasResponseDTO;
import br.com.topsorteio.dtos.MarcaEditRequestDTO;
import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
import br.com.topsorteio.service.MarcaService;

@RestController
@RequestMapping("/marcas")
public class MarcaController {
	
	@Autowired
	private MarcaService repository;
	
	@GetMapping
	@RequestMapping("/obter")
	public ResponseEntity<List<GetAllMarcasResponseDTO>> obterTodasAsMarcas(){
		return repository.obterTodasAsMarcas();
	}
	
	@GetMapping
	@RequestMapping("/obter/{id}")
	public ResponseEntity<?> obterMarcaPorId(@PathVariable Integer id){
		return repository.obterMarcaPorId(id);
	}
	
	@PostMapping
	@RequestMapping("/registrar")
	public ResponseEntity<?> registrarMarca(@RequestBody MarcaRegisterRequestDTO request){
		return repository.registrarMarca(request);
	}
	
	@PutMapping
	@RequestMapping("/editar/{id}")
	public ResponseEntity<?> editarMarca(@PathVariable Integer id, @RequestBody MarcaEditRequestDTO request){
		return repository.editarMarca(id, request);
	}
	
	
}