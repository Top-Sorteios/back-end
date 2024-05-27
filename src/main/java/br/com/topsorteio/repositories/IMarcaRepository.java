package br.com.topsorteio.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.topsorteio.entities.MarcaModel;

public interface IMarcaRepository extends JpaRepository<MarcaModel, Integer>{

	Optional<MarcaModel> findByNome(String nome);

}
