package br.com.topsorteio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.security.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;


@Entity(name = "TBUsuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "TBUsuario")
public class UserModel {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UsuarioID", nullable = false)
    private Integer id;

    @Column(name = "Nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "Cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "Senha", nullable = true)
    private String senha;

    @Column(name = "DataNascimento", nullable = false)
    private Date dataNascimento;

    @Column(name = "Turma", nullable = false, length = 50)
    private String turma;

    @Column(name = "Status", nullable = true, length = 20)
    private String status;

    @Column(name = "Administrador", nullable = false)
    private boolean adm;

    @Column(name = "CriadoPor", nullable = true)
    private int criadoPor;

    @Column(name = "CriadoEm", nullable = false, columnDefinition = "Timestamp", updatable = true)
    private Date criadoEm = new Date();
}
