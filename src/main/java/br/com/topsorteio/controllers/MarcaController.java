package br.com.topsorteio.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.topsorteio.dtos.ErrorDTO;
import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.service.MarcaService;

@RestController
@CrossOrigin("*")
@RequestMapping("/marcas")
public class MarcaController {
	
	@Autowired
	private MarcaService marcaService;
	
	@PostMapping
	@RequestMapping("/inserir")
	public ResponseEntity<?> inserirMarca(@RequestBody MarcaRegisterRequestDTO request){
		Optional<MarcaModel> marca = this.marcaService.findByName(request.nome());
		if(marca.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "Marca já existe.", false));
		return ResponseEntity.status(201).body(marcaService.inserirMarca(new MarcaModel(request)));
	}
}
