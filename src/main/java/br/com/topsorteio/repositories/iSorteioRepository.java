package br.com.topsorteio.repositories;

import br.com.topsorteio.entities.SorteioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iSorteioRepository extends JpaRepository<SorteioModel, Integer> {
}
