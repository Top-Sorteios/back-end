package br.com.topsorteio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbturma")
public class TurmaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turmaid", nullable = true)
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "participando_sorteio", nullable = false)
    private boolean participandoSorteio;

    @Column(name = "criadoem", nullable = false)
    private Date criadoem = new Date();

    @OneToMany
    @JoinColumn(name = "turmaid")
    private List<UserModel> usuarios;

    public TurmaModel(String nome, boolean participandoSorteio){
        this.nome = nome;
        this.participandoSorteio = participandoSorteio;
    }

}
