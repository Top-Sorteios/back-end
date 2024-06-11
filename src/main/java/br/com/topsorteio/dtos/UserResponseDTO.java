package br.com.topsorteio.dtos;


import br.com.topsorteio.entities.UserRole;

public record UserResponseDTO(String nome, String email, String cpf, UserRole administrador, String status, String datanascimento, String turma) {
}