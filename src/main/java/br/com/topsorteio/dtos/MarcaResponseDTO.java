package br.com.topsorteio.dtos;

import java.util.Date;

public record MarcaResponseDTO(Integer id, String nome, String titulo, int ordemExibicao, byte[] logo, byte[] banner, String criadoPor, String criadoEm) {
}
