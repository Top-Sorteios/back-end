package br.com.topsorteio.dtos;

import java.util.Date;

public record UserRegisterRequestDTO(String nome, String cpf, String email, String senha,
                                     Date datanascimento, String turma, String status,
                                     boolean administrador, Integer criadopor) {}