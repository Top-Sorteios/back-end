package br.com.topsorteio.entities;

import br.com.topsorteio.dtos.UserRegisterRequestDTO;
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
public class UserModel implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioid", nullable = false)
    private Integer id;

    @Column(name = "turmaid", nullable = false)
    private Integer turma;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = true)
    private String senha;

    @Column(name = "datanascimento", nullable = false)
    private String dataNascimento;


    @Column(name = "status", nullable = true, length = 20)
    private String status;

    @Column(name = "administrador", nullable = false)
    private UserRole adm;

    @Column(name = "participando_sorteio", nullable = false)
    private boolean participandoSorteio;

    @Column(name = "criadopor", nullable = true)
    private int criadoPor;

    @Column(name = "criadoem", nullable = false)
    private Date criadoEm = new Date();


    public UserModel(UserRegisterRequestDTO data) {
        this.nome = data.nome();
        this.cpf = data.cpf();
        this.email = data.email();;
        this.dataNascimento = data.datanascimento();
        this.turma = data.turma();
        this.status = data.status();
        this.adm = data.administrador();
        this.criadoPor = data.criadopor();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.adm == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
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
