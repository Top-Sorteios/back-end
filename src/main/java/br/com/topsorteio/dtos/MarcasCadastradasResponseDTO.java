package br.com.topsorteio.dtos;

import java.util.Date;

public record MarcasCadastradasResponseDTO(Integer id, String nome, String criadoPor, Date criadoEm) {

}
