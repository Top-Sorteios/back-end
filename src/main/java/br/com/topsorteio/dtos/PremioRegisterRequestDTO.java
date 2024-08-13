package br.com.topsorteio.dtos;

import org.springframework.web.multipart.MultipartFile;

public record PremioRegisterRequestDTO(String nome, String codigoSku, String descricao, Integer quantidade, MultipartFile imagem, Integer marcaId) {
}
