package br.com.topsorteio.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@Table(name = "tbusuario")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioid", nullable = false)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = true)
    private String senha;

    @Column(name = "datanascimento", nullable = false)
    private Date dataNascimento;

    @Column(name = "turma", nullable = false, length = 50)
    private String turma;

    @Column(name = "status", nullable = true, length = 20)
    private String status;

    @Column(name = "administrador", nullable = false)
    private boolean adm;

    @Column(name = "criadopor", nullable = true)
    private int criadoPor;

    @Column(name = "criadoem", nullable = false)
    private Date criadoEm = new Date();
}
