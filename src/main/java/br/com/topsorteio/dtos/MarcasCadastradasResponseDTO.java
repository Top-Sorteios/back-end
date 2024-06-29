package br.com.topsorteio.dtos;


public record MarcasCadastradasResponseDTO(Integer id, String nome, String titulo, byte[] logo, byte[] banner, int ordemExibicao, String criadoPor, String criadoEm) {
}
