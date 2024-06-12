package br.com.topsorteio.dtos;

public record PremioRegisterRequestDTO(String nome, String codigoSku, byte[] imagem, int quantidade, String descricao, Integer marcaId, Integer criadoPor) {
}
