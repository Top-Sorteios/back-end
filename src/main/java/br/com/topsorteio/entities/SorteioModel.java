package br.com.topsorteio.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@Entity(name = "tbsorteio")
@NoArgsConstructor
@AllArgsConstructor
public class SorteioModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sorteioid")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "premioid")
    private PremioModel premio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioid")
    private UserModel usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turmaid")
    private TurmaModel turma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criadopor")
    private UserModel criadopor;

    @Column(name = "premio_surpresa")
    private boolean premioSurpresa;

    @Column(name = "criadoem")
    private Date criadoem = new Date();

    public SorteioModel(PremioModel premio, UserModel usuario, TurmaModel turma, UserModel criadopor, boolean premioSurpresa){
        this.premio = premio;
        this.usuario = usuario;
        this.turma = turma;
        this.criadopor = criadopor;
        this.premioSurpresa = premioSurpresa;
    }
}
