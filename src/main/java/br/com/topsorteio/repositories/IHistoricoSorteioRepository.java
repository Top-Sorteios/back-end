package br.com.topsorteio.repositories;

import br.com.topsorteio.entities.HistoricoSorteioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IHistoricoSorteioRepository extends JpaRepository<HistoricoSorteioModel, Integer> {
    List<HistoricoSorteioModel> findByTurmaNome(String turmaNome);
}
