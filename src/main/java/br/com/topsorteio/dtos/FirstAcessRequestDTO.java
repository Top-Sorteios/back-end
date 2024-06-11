package br.com.topsorteio.dtos;

import java.util.Date;

public record FirstAcessRequestDTO(String email, String cpf, String datanascimento, String senha) {}