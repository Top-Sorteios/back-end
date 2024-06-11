package br.com.topsorteio.dtos;

public record MarcaRegisterRequestDTO(String nome, String titulo, byte[] logo, byte[] banner, int ordemExibicao, Integer criadoPor) {}
