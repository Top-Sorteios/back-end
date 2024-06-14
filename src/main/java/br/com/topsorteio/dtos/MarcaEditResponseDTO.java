package br.com.topsorteio.dtos;

public record MarcaEditResponseDTO(Integer id, String nome, String titulo, int ordemExibicao, byte[] logo, byte[] banner) {
}
