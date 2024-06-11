package br.com.topsorteio.dtos;

public record TokenResponseDTO(String email, String token, boolean status) { }