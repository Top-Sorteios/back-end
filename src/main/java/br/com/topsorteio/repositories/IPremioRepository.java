package br.com.topsorteio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.topsorteio.entities.PremioModel;

@Repository
public interface IPremioRepository extends JpaRepository<PremioModel, Integer> {

	Optional<PremioModel> findByNome(String nome);

	Optional<PremioModel> findByCodigoSku(String codigoSku);

	List<PremioModel> findByNomeContainingIgnoreCase(String nome);

}
