package br.com.topsorteio.dtos;

public record MarcaResponseDTO(Integer id, String nome, String titulo, int ordemExibicao, byte[] logo, byte[] banner) {
}
