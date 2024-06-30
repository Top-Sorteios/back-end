package br.com.topsorteio.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.topsorteio.dtos.*;
import br.com.topsorteio.entities.PremioModel;
import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.repositories.IPremioRepository;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.orm.jpa.JpaSystemException;
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

	@Autowired
	private UserService userService;

	public ResponseEntity<List<MarcasCadastradasResponseDTO>> obterTodasAsMarcas(){
		try {
			List<MarcaModel> marcas = marcaRepository.findAll();
			if(marcas.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


			List<MarcasCadastradasResponseDTO> response = new ArrayList<>();
			for(MarcaModel marca : marcas){

				response.add(new MarcasCadastradasResponseDTO(
						marca.getId(),
						marca.getNome(),
						marca.getTitulo(),
						marca.getLogo(),
						marca.getBanner(),
						marca.getOrdemExibicao(),
						marca.getCriadoPor().getNome(),
						new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(marca.getCriadoEm()))
				);
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new EventInternalServerErrorException(e.getMessage());
		}
	}
	
	 public ResponseEntity<?> obterMarcaPorId(Integer id){
		 try {
			 MarcaModel marca = marcaRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Marca com ID " + id + " não encontrada."));

			 MarcaResponseDTO response = new MarcaResponseDTO(
					 marca.getId(),
					 marca.getNome(),
					 marca.getTitulo(),
					 marca.getOrdemExibicao(),
					 marca.getLogo(),
					 marca.getBanner(),
					 marca.getCriadoPor().getNome(),
					 new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(marca.getCriadoEm())
			 );

			 return new ResponseEntity<>(response, HttpStatus.OK);
		 } catch (JpaSystemException e) {
			 throw new EventInternalServerErrorException(e.getMessage());
		 }
	 }
	
	public ResponseEntity<?> registrarMarca(MarcaRegisterRequestDTO request){
		try {
			MarcaModel marca = new MarcaModel(request);
			UserModel user = userService.getAuthenticatedUser();
			marca.setCriadoPor(user);
			marcaRepository.save(marca);
			return new ResponseEntity<>("Marca registrada com sucesso.", HttpStatus.CREATED);
		} catch (IOException e) {
			throw new EventInternalServerErrorException(e.getMessage());
		}
	}
	
	public ResponseEntity<?> editarMarca(Integer id, MarcaEditRequestDTO request) {
		try {
			MarcaModel marca = marcaRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Marca com ID " + id + " não encontrada."));

			marca.setNome(request.nome());
			marca.setTitulo(request.titulo());
			marca.setLogo(request.logo().getBytes());
			marca.setBanner(request.banner().getBytes());
			marca.setOrdemExibicao(request.ordemExibicao());

			marcaRepository.save(marca);

			return new ResponseEntity<>("Marca editada com sucesso.", HttpStatus.OK);
		} catch (IOException e) {
			throw new EventInternalServerErrorException(e.getMessage());
		}
	}
	
	public ResponseEntity<?> removerMarca(Integer id) {
		try {
			marcaRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Marca com ID " + id + " não encontrada."));
			marcaRepository.deleteById(id);
			return new ResponseEntity<>("Marca removida com sucesso.", HttpStatus.OK);
		} catch (JpaSystemException e) {
			throw new EventInternalServerErrorException(e.getMessage());
		}
	}

	public ResponseEntity<?> obterMarcasParaVitrine() {
		try {
			List<PremioModel> premios = premioRepository.findAll();
			List<MarcasVitrineResponseDTO> response = new ArrayList<>();
			for(PremioModel premio : premios){
				response.add(new MarcasVitrineResponseDTO(
						premio.getMarca().getNome(),
						premio.getMarca().getTitulo(),
						premio.getDescricao(),
						premio.getMarca().getLogo()));
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new EventInternalServerErrorException(e.getMessage());
		}
	}


}
