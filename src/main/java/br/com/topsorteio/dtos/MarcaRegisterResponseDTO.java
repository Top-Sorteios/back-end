package br.com.topsorteio.dtos;

public record MarcaRegisterResponseDTO(Integer id, String nome, String titulo, byte[] logo, byte[] banner, int ordemExibicao, Long criadoPor) {
}
