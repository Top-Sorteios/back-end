package br.com.topsorteio.dtos;

import java.util.Date;

public record PremiosCadastradosResponseDTO(Integer id, String nome, String codigoSku, String descricao, byte[] imagem, String marcaNome, String criadoPor, Date criadoEm) {
}
