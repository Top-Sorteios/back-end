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
    @JoinColumn(name = "criadopor")
    private UserModel criadopor;

    @Column(name = "criadoem")
    private Date criadoem = new Date();

    public SorteioModel(PremioModel premio, UserModel usuario, UserModel criadopor){
        this.premio = premio;
        this.usuario = usuario;
        this.criadopor = criadopor;
    }
}
