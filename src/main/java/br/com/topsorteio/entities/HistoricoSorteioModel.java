package br.com.topsorteio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbhistoricosorteio")
public class HistoricoSorteioModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historicoid", nullable = false)
    private Integer historicoId;

    @Column(name = "ganhadornome", nullable = false)
    private String ganhadorNome;

    @Column(name = "ganhadorcpf", nullable = false)
    private String ganhadorCPF;

    @Column(name = "ganhadoremail", nullable = false)
    private String ganhadorEmail;

    @Column(name = "ganhadordatanascimento", nullable = false)
    private String ganhadorDataNascimento;

    @Column(name = "premionome", nullable = false)
    private String premioNome;

    @Column(name = "premiosku", nullable = false)
    private String premioSku;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "premioimagem")
    private byte[] premioImagem;

    @Column(name = "premiodescricao", nullable = false)
    private String premioDescricao;

    @Column(name = "premiosurpresa", nullable = false)
    private boolean premioSurpresa;

    @Column(name = "turmanome", nullable = false)
    private String turmaNome;

    @Column(name = "marcanome", nullable = false)
    private String marcaNome;

    @Column(name = "sorteadoem", nullable = false)
    private Date sorteadoEm;

    @Column(name = "sorteadopor", nullable = false)
    private String sorteadoPor;

    @Column(name = "criadoem", nullable = false)
    private java.util.Date criadoEm = new java.util.Date();


}
