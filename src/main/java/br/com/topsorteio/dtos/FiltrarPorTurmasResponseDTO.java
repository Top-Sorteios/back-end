package br.com.topsorteio.dtos;

import br.com.topsorteio.entities.HistoricoSorteioModel;

import java.util.List;


public record FiltrarPorTurmasResponseDTO(List<HistoricoSorteioModel> turmas) {
}
