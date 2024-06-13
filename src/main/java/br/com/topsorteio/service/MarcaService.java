package br.com.topsorteio.service;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.topsorteio.dtos.MarcasCadastradasResponseDTO;
import br.com.topsorteio.dtos.MarcaEditRequestDTO;
import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.repositories.IMarcaRepository;

@Service
public class MarcaService {
	
	@Autowired
	private IMarcaRepository marcaRepository;

	@Autowired
	private iUserRepository userRepository;
	
	public ResponseEntity<List<MarcasCadastradasResponseDTO>> obterTodasAsMarcas(){
		List<MarcaModel> marcas = marcaRepository.findAll();
        List<MarcasCadastradasResponseDTO> response = new ArrayList<>();

        if(marcas.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        for(MarcaModel marca : marcas)
            response.add(new MarcasCadastradasResponseDTO(marca.getNome(), marca.getCriadoPor().getNome(), marca.getCriadoEm()));

        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	 public ResponseEntity<Optional<MarcaModel>> obterMarcaPorId(Integer id){
        Optional<MarcaModel> marca = marcaRepository.findById(id);
        if(marca.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(marca, HttpStatus.OK);
    }
	
	public ResponseEntity<?> registrarMarca(MarcaRegisterRequestDTO request) {
		Optional<UserModel> userOpt = userRepository.findById(request.criadoPor());
		if (userOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		MarcaModel marca = new MarcaModel(request, userOpt.get());
		MarcaModel marcaSalva = marcaRepository.save(marca);

		return ResponseEntity.status(HttpStatus.CREATED).body(marcaSalva);
	}
	
	public ResponseEntity<?> editarMarca(Integer id, MarcaEditRequestDTO request) {
		Optional<MarcaModel> marca = marcaRepository.findById(id);
		if(marca.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		MarcaModel marcaSalva = marca.get();
		marcaSalva.setNome(request.nome());
		marcaSalva.setTitulo(request.titulo());
		marcaSalva.setLogo(request.logo());
		marcaSalva.setBanner(request.banner());
		marcaSalva.setOrdemExibicao(request.ordemExibicao());
	    marcaRepository.save(marcaSalva);
	    return new ResponseEntity<>(marcaSalva, HttpStatus.OK);
	}
	
	public ResponseEntity<?> removerMarca(Integer id) {
		Optional<MarcaModel> marca = marcaRepository.findById(id);
		if(marca.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		marcaRepository.deleteById(id);
		return new ResponseEntity<>("Marca de ID " + id + " removida com sucesso.", HttpStatus.OK);
	}
}
