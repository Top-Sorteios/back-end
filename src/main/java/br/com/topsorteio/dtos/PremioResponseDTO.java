package br.com.topsorteio.dtos;

import java.util.Date;
import java.util.List;

public record PremioResponseDTO(Integer id, String nome, String codigoSku, String descricao, int quantidade, byte[] imagem, String criadoPor, Date criadoEm, List<String> opcoesMarcas, List<byte[]> MarcasLogos) {
}
