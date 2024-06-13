package br.com.topsorteio.dtos;

import java.util.Date;

public record GetAllMarcasResponseDTO(String nome, String titulo, byte[] logo, byte[] banner, int ordemExibicao, Integer criadopor, Date criadoem) {

}
