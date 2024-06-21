package br.com.topsorteio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.repositories.IPremioRepository;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.repositories.IMarcaRepository;

@Service
public class MarcaService {
	
	@Autowired
	private IMarcaRepository marcaRepository;

	@Autowired
	private iUserRepository userRepository;

	@Autowired
	private IPremioRepository premioRepository;

	public ResponseEntity<List<MarcasCadastradasResponseDTO>> obterTodasAsMarcas(){
		List<MarcaModel> marcas = marcaRepository.findAll();
		List<MarcasCadastradasResponseDTO> response = new ArrayList<>();

		if(marcas.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		for(MarcaModel marca : marcas)
			response.add(new MarcasCadastradasResponseDTO(
					marca.getId(),
					marca.getNome(),
					marca.getTitulo(),
					marca.getLogo(),
					marca.getBanner(),
					marca.getOrdemExibicao(),
					marca.getCriadoPor().getNome(),
					marca.getCriadoEm()));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	 public ResponseEntity<?> obterMarcaPorId(Integer id){
        Optional<MarcaModel> marcaOpt = marcaRepository.findById(id);
        if(marcaOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		MarcaModel marca = marcaOpt.get();
		MarcaResponseDTO response = new MarcaResponseDTO(
				marca.getId(),
				marca.getNome(),
				marca.getTitulo(),
				marca.getOrdemExibicao(),
				marca.getLogo(),
				marca.getBanner());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	public ResponseEntity<?> registrarMarca(MarcaRegisterRequestDTO request) throws IOException {
		Optional<UserModel> userOpt = userRepository.findById(request.criadoPor());
		if (userOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		MarcaModel marca = new MarcaModel(request, userOpt.get());

		marcaRepository.save(marca);

		MarcaRegisterResponseDTO response = new MarcaRegisterResponseDTO(
				marca.getId(),
				marca.getNome(),
				marca.getTitulo(),
				marca.getLogo(),
				marca.getBanner(),
				marca.getOrdemExibicao(),
				userOpt.get().getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	public ResponseEntity<?> editarMarca(Integer id, MarcaEditRequestDTO request) throws IOException {
		Optional<MarcaModel> marcaOpt = marcaRepository.findById(id);
		if(marcaOpt.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		MarcaModel marca = marcaOpt.get();
		marca.setNome(request.nome());
		marca.setTitulo(request.titulo());
		marca.setLogo(request.logo().getBytes());
		marca.setBanner(request.banner().getBytes());
		marca.setOrdemExibicao(request.ordemExibicao());

	    marcaRepository.save(marca);

		MarcaEditResponseDTO response = new MarcaEditResponseDTO(
				marca.getId(),
				marca.getNome(),
				marca.getTitulo(),
				marca.getOrdemExibicao(),
				marca.getLogo(),
				marca.getBanner());
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<?> removerMarca(Integer id) {
		Optional<MarcaModel> marca = marcaRepository.findById(id);
		if(marca.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		marcaRepository.deleteById(id);
		return new ResponseEntity<>("Marca de ID " + id + " removida com sucesso.", HttpStatus.OK);
	}

	public ResponseEntity<?> obterMarcasParaVitrine() {
		List<PremioModel> premios = premioRepository.findAll();
		List<MarcasVitrineResponseDTO> response = new ArrayList<>();
		for(PremioModel premio : premios){
			response.add(new MarcasVitrineResponseDTO(
					premio.getMarca().getNome(),
					premio.getMarca().getTitulo(),
					premio.getDescricao(),
					premio.getMarca().getLogo()
			));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
