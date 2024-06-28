package br.com.topsorteio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagem")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criadopor", nullable = false)
    private UserModel criadoPor;

    @Column(name = "criadoem", nullable = false)
    private Date criadoEm = new Date();
}
