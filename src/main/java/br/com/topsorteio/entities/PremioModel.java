package br.com.topsorteio.entities;

import br.com.topsorteio.dtos.PremioRegisterRequestDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name="tbpremios")
public class PremioModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "premioid", nullable = false)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name="codigosku", nullable=false, length = 50, unique = true)
    private String codigoSku;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagem")
    private byte[] imagem;

    @Column (name="quantidade", nullable = false)
    private int quantidade;

    @Column(name ="descricao", nullable = false, length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marcaid", nullable = false)
    private MarcaModel marca;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "criadopor", nullable = false)
    private UserModel criadoPor;

    @Column(name="criadoem", nullable=false)
    private Date criadoEm = new Date();

    public PremioModel(PremioRegisterRequestDTO data, MarcaModel marca, UserModel user) {
        this.nome = data.nome();
        this.codigoSku = data.codigoSku();
        this.descricao = data.descricao();
        this.quantidade = data.quantidade();
        this.imagem = data.imagem();
        this.marca = marca;
        this.criadoPor = user;
    }
}
