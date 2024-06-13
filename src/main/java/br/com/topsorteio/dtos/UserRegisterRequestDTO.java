package br.com.topsorteio.dtos;

import br.com.topsorteio.entities.TurmaModel;

public record UserRegisterRequestDTO(String nome, String cpf, String email,
                                     String datanascimento, TurmaModel turma, String status,
                                     boolean administrador, int criadopor) {}