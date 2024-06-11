package br.com.topsorteio.dtos;

public record PremioEditRequestDTO(String nomeAtual, String nome, int quantidadeAtual, int quantidade, 
								String descricaoAtual, String descricao, byte[] imagemAtual, byte[] imagem
								) {}
