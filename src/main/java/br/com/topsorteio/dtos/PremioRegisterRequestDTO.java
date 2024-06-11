package br.com.topsorteio.dtos;

import br.com.topsorteio.entities.MarcaModel;

public record PremioRegisterRequestDTO (String codigoSku, String nome, int quantidade, 
										String descricao, byte[] imagem, MarcaModel marca) {}
 

