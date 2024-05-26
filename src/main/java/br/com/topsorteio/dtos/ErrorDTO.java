package br.com.topsorteio.dtos;

import org.springframework.http.HttpStatus;


public record ErrorDTO(HttpStatus error, int code, String mensagem, boolean status) { }
