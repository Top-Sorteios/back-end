package br.com.topsorteio.entities;

import br.com.topsorteio.dtos.DestaqueIndexRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbdestaqueindex")
public class DestaqueIndexModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destaqueindexid")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "titulo")
    private String titulo;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagem")
    private byte[] imagem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "criadopor", nullable = false)
    private UserModel criadoPor;

    @Column(name = "criadoem", nullable = false)
    private Date criadoEm = new Date();

    public DestaqueIndexModel(DestaqueIndexRequestDTO data) throws IOException {
        this.nome = data.nome();
        this.titulo = data.titulo();
        this.imagem = data.imagem().getBytes();
    }
}
