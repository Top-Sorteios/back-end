package br.com.topsorteio.dtos;

import java.util.Date;

public record PremioRegisterResponseDTO(Integer id, String nome, String codigoSku, byte[] imagem, int quantidade, String descricao, Integer marcaId, Long criadoPor, Date criadoEm) {
}
