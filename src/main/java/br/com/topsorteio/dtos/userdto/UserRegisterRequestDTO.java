package br.com.topsorteio.dtos.userdto;

import java.util.Date;

public record UserRegisterRequestDTO(String nome, String cpf, String email, Date data_nascimento, String turma) {}