package br.com.topsorteio.dtos;

public record PremioEditRequestDTO(String nome, String codigoSku, byte[] imagem, int quantidade, String descricao, Integer marcaId) {
}
