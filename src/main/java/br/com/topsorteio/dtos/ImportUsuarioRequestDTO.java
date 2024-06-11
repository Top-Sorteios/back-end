package br.com.topsorteio.dtos;

import org.springframework.web.multipart.MultipartFile;

public record ImportUsuarioRequestDTO(MultipartFile file) {}
