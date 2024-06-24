package br.com.topsorteio.dtos;

import br.com.topsorteio.entities.HistoricoSorteioModel;

import java.util.List;

public record ObterHistoricoSorteioPorTurmaResponseDTO(List<HistoricoSorteioModel> historicoTurma) {
}