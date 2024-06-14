package br.com.topsorteio.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@Table(name = "tbusuario")
@NamedStoredProcedureQuery(
        name = "SP_ObterUsuariosParaSorteio_V2_S",
        procedureName = "SP_ObterUsuariosParaSorteio_V2_S",
        parameters = {
                @StoredProcedureParameter(name = "SORTEIO_SURPRESA", mode = ParameterMode.IN, type = String.class),
        }
)
public class UserModel implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turmaid")
    private TurmaModel turma;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = true)
    private String senha;

    @Column(name = "datanascimento", nullable = false)
    private String dataNascimento;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "administrador", nullable = false)
    private boolean adm;

    @Column(name = "participando_sorteio", nullable = false)
    private boolean participandoSorteio;

    @Column(name = "criadopor", nullable = false)
    private int criadoPor;

    @Column(name = "criadoem", nullable = false)
    private Date criadoEm = new Date();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.adm) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }


    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
