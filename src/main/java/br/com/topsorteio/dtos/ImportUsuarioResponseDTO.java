package br.com.topsorteio.dtos;

import org.springframework.http.HttpStatus;

public record ImportUsuarioResponseDTO(HttpStatus status, String mensagem) {}
