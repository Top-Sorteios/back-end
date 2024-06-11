package br.com.topsorteio.dtos;

public record GetAllMarcasResponseDTO(String nome, String titulo, byte[] logo, byte[] banner, int ordemExibicao) {

}