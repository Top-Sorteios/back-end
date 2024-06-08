package br.com.topsorteio.service;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.topsorteio.dtos.ErrorDTO;
import br.com.topsorteio.dtos.GetAllMarcasResponseDTO;
import br.com.topsorteio.dtos.MarcaEditRequestDTO;
import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.repositories.IMarcaRepository;

@Service
public class MarcaService {
	
	@Autowired
	private IMarcaRepository repository;
	
	public ResponseEntity<List<GetAllMarcasResponseDTO>> obterTodasAsMarcas(){
		List<MarcaModel> marcas = repository.findAll();
        List<GetAllMarcasResponseDTO> response = new ArrayList<>();

        if(marcas.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        for(MarcaModel marca : marcas)
            response.add(new GetAllMarcasResponseDTO(marca.getNome(), marca.getTitulo(), marca.getLogo(), marca.getBanner(), marca.getOrdemExibicao()));

        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	 public ResponseEntity<Optional<MarcaModel>> obterMarcaPorId(Integer id){
        Optional<MarcaModel> marca = repository.findById(id);
        if(marca.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(marca, HttpStatus.OK);
    }
	
	public ResponseEntity<?> registrarMarca(@RequestBody MarcaRegisterRequestDTO request) {
		Optional<MarcaModel> marca = repository.findByNome(request.nome());
		if(marca.isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(HttpStatus.CONFLICT, 409, "Marca já existe.", false));
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(new MarcaModel(request)));
	}
	
	public ResponseEntity<?> editarMarca(@PathVariable Integer id, @RequestBody MarcaEditRequestDTO request) {
		Optional<MarcaModel> marca = repository.findById(id);
		if(marca.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		MarcaModel marcaSalva = marca.get();
		marcaSalva.setNome(request.nome());
		marcaSalva.setTitulo(request.titulo());
		marcaSalva.setLogo(request.logo());
		marcaSalva.setBanner(request.banner());
		marcaSalva.setOrdemExibicao(request.ordemExibicao());
	    repository.save(marcaSalva);
	    return new ResponseEntity<>(marcaSalva, HttpStatus.OK);
	}
	
	public ResponseEntity<?> removerMarca(@PathVariable Integer id) {
		Optional<MarcaModel> marca = repository.findById(id);
		if(marca.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repository.deleteById(id);
		return new ResponseEntity<>("Marca de ID " + id + " removida com sucesso.", HttpStatus.OK);
	}
}
