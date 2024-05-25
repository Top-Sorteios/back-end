package br.com.topsorteio.dtos;

import br.com.topsorteio.entities.UserRole;

import java.util.Date;

public record UserRegisterRequestDTO(String nome, String cpf, String email, String senha,
                                     Date datanascimento, String turma, String status,
                                     UserRole administrador, Integer criadopor) {}