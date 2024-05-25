package br.com.topsorteio.dtos;


import br.com.topsorteio.entities.UserRole;

public record GetAllUserResponseDTO(String nome, String email, UserRole administrador, String status) {
}
