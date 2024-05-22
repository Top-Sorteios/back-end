package br.com.topsorteio.dtos.userdto;


import br.com.topsorteio.entities.user.UserRole;

public record UserResponseDTO(String nome, String email, UserRole administrador, String status) {
}
