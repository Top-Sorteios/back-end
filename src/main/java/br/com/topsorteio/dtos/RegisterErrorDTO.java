package br.com.topsorteio.dtos;

import org.springframework.http.HttpStatus;


public record RegisterErrorDTO(HttpStatus status, int code, String mensagem) { }
