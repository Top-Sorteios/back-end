package br.com.topsorteio.dtos;

import java.util.Date;

public record MarcasCadastradasResponseDTO(Integer id, String nome, String titulo, byte[] logo, byte[] banner, int ordemExibicao, String criadoPor, Date criadoEm) {
}
