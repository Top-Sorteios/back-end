package br.com.topsorteio.dtos.userdto;

public record UserResponseDTO(String nome, String email, boolean administrador, String status) {
}
