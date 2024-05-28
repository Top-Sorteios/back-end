package br.com.topsorteio.service;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import br.com.topsorteio.entities.MarcaModel;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import br.com.topsorteio.repositories.IMarcaRepository;

@Service
public class MarcaService {
	
	@Autowired
	private IMarcaRepository repository;
	

	public List<MarcaModel> findAll(MarcaModel marca) {
		try{
            return repository.findAll();
        } catch (JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
	}
	
	public Optional<MarcaModel> findByName(String nome){
        try{
            return repository.findByNome(nome);
        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }
	
	public MarcaModel inserirMarca(MarcaModel marca) {
		return repository.save(marca);
	}
	
	public MarcaModel editarMarca(MarcaModel marca) {
		try{
            return repository.save(marca);
        } catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
	}
	
	public Boolean removerMarca(Integer id) {
		repository.deleteById(id);
		return true;
	}
}
