package br.com.topsorteio.repositories;

import br.com.topsorteio.entities.PremioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPremioRepository extends JpaRepository<PremioModel, Integer> {
    Optional<PremioModel> findByCodigoSku(String codigoSku);
}
