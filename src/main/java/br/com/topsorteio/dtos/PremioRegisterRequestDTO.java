package br.com.topsorteio.dtos;

public record PremioRegisterRequestDTO(String nome, String codigoSku, String descricao, int quantidade, byte[] imagem,  Integer marcaId, Integer criadoPor) {
}
