package br.com.topsorteio.controllers;

import java.util.List;

import br.com.topsorteio.dtos.MarcasCadastradasResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.topsorteio.dtos.MarcaEditRequestDTO;
import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
import br.com.topsorteio.service.MarcaService;

@RestController
@CrossOrigin("*")
@RequestMapping("/marcas")
public class MarcaController {

	@Autowired
	private MarcaService repository;

	@GetMapping("/obter")
	public ResponseEntity<List<MarcasCadastradasResponseDTO>> obterTodasAsMarcas(){
		return repository.obterTodasAsMarcas();
	}

	@GetMapping("/obter/{id}")
	public ResponseEntity<?> obterMarcaPorId(@PathVariable Integer id){

		return repository.obterMarcaPorId(id);
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registrarMarca(@RequestBody MarcaRegisterRequestDTO request){
		return repository.registrarMarca(request);
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> editarMarca(@PathVariable Integer id, @RequestBody MarcaEditRequestDTO request){
		return repository.editarMarca(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerMarca(@PathVariable Integer id){
		return repository.removerMarca(id);
	}

	@GetMapping("/vitrine")
	public ResponseEntity<?> obterMarcasParaVitrine(){
		return repository.obterMarcasParaVitrine();
	}
}
