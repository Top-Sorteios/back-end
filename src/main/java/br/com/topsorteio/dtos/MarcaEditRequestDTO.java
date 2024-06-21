package br.com.topsorteio.dtos;

import org.springframework.web.multipart.MultipartFile;

public record MarcaEditRequestDTO(String nome, String titulo, MultipartFile logo, MultipartFile banner, int ordemExibicao) {

}