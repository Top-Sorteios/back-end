package br.com.topsorteio.dtos;

import br.com.topsorteio.entities.HistoricoSorteioModel;

import java.util.Date;
import java.util.List;


public record FiltrarPorTurmasResponseDTO(
        String ganhadorNome,
        String ganhadorEmail,
        String ganhadorDataNascimento,
        String premioNome,
        String premioSku,
        byte[] premioImagem,
        String premioDescricao,
        boolean premioSurpresa,
        String turmaNome,
        String marcaNome,
        Date sorteadoEm,
        String sorteadoPor,
        String criadoEm

        ) {
}
