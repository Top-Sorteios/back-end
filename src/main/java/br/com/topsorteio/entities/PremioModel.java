//package br.com.topsorteio.entities;
//
//import java.util.Date;
//
//import br.com.topsorteio.dtos.MarcaRegisterRequestDTO;
//import jakarta.persistence.Basic;
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.Lob;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Entity
//@NoArgsConstructor
//@Table(name="tbpremios")
//public class PremioModel {
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="premiosku", nullable=false, length = 30)
//	private String sku;
//
//	@Column(name = "nome", nullable = false, length = 100)
//    private String nome;
//
//	@Column (name="quantidade", nullable = false)
//	private int quantidade;
//
//	@Column(name ="descrição", nullable = false, length = 300)
//	private String descricao;
//
//	@Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Column(name = "imagem", nullable = false)
//    private byte[] imagem;
//
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "marca_id", nullable = false)
//    private MarcaModel marca;
//
//    // Constructor with all attributes
//    public PremioModel(String sku, String nome, int quantidade, String descricao, byte[] imagem, MarcaModel marca) {
//        this.sku = sku;
//        this.nome = nome;
//        this.quantidade = quantidade;
//        this.descricao = descricao;
//        this.imagem = imagem;
//        this.marca = marca;
//    }
//}
