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

    @Column(name = "premioid")
    private Integer premioid;

    @Column(name = "usuarioid")
    private Integer usuarioid;

    @Column(name = "criadopor")
    private Integer criadopor;

    @Column(name = "criadorem")
    private Date criadoem = new Date();
}
