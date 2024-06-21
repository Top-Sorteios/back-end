package br.com.topsorteio.entities;

import java.io.IOException;
import java.util.Date;

import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="tbmarca")
public class MarcaModel {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="marcaid", nullable=false)
	private Integer id;

	@Column(name="nome", length=100, nullable=false)
	private String nome;

	@Column(name="titulo", length=100, nullable=false)
	private String titulo;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="logo")
	private byte[] logo;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="banner")
	private byte[] banner;

	@Column(name="ordemexibicao", nullable=false)
	private int ordemExibicao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "criadopor", nullable = false)
	private UserModel criadoPor;

	@Column(name="criadoem", nullable=false)
	private Date criadoEm = new Date();

	public MarcaModel(MarcaRegisterRequestDTO data, UserModel user) throws IOException {
		this.nome = data.nome();
		this.titulo = data.titulo();
		this.logo = data.logo().getBytes();
		this.banner = data.banner().getBytes();
		this.ordemExibicao = data.ordemExibicao();
		this.criadoPor = user;
	}
}