package br.com.topsorteio.dtos;

import org.springframework.web.multipart.MultipartFile;

public record PremioEditRequestDTO(String nome, String codigoSku, MultipartFile imagem, int quantidade, String descricao, Integer marcaId) {
}
