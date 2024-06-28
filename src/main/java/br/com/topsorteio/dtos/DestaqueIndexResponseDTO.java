package br.com.topsorteio.dtos;

import java.util.Date;

public record DestaqueIndexResponseDTO(Long id, String nome, String titulo, byte[] imagem, String criadoPor, Date criadoEm) {
}
