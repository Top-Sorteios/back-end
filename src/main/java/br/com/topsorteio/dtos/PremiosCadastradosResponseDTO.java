package br.com.topsorteio.dtos;

import java.util.Date;

public record PremiosCadastradosResponseDTO(Integer id, String nome, String marcaNome, String criadoPor, Date criadoEm) {
}
