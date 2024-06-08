package br.com.topsorteio.entities;

import java.util.Date;

import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="tbmarcas")
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
	@Column(name="logo", nullable=true)
	private byte[] logo;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="banner", nullable=true)
	private byte[] banner;
	
	@Column(name="ordem_exibicao", nullable=false)
	private int ordemExibicao;
	
	@Column(name="criado_por", nullable=true)
	private Integer criadoPor;
	
	@Column(name="criado_em", nullable=false)
	private Date criadoEm = new Date();
	
	public MarcaModel(MarcaRegisterRequestDTO data) {
        this.nome = data.nome();
        this.titulo = data.titulo();
		this.logo = data.logo();
		this.banner = data.banner();
	    this.ordemExibicao = data.ordemExibicao();;
	    this.criadoPor = data.criadoPor();
    }
}
