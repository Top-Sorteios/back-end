package br.com.topsorteio.dtos;

public record PremioEditResponseDTO(Integer id, String nome, String codigoSku, byte[] imagem, int quantidade, String descricao, Integer marcaId) {
}
