package br.com.topsorteio.dtos;

public record GetAllPremiosResponseDTO(String nome, String codigoSku, byte[] imagem, int quantidade, String descricao, Integer marcaid, String marcaNome) {
}
