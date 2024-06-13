package br.com.topsorteio.dtos;


import br.com.topsorteio.entities.TurmaModel;


public record UserResponseDTO(String nome, String email, String cpf, boolean administrador, String status, String datanascimento, TurmaResponseDTO turma) {
}
