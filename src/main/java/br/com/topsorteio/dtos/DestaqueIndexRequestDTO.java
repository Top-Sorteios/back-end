package br.com.topsorteio.dtos;

import org.springframework.web.multipart.MultipartFile;

public record DestaqueIndexRequestDTO(String nome, String titulo, MultipartFile imagem) {
}
