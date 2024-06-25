package br.com.topsorteio.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/usuarios/login", "/usuarios/esqueci-senha").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/primeiro-acesso").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/helloworld", "/usuarios/premio").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/obter/{email}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/editar/senha/{email}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/marcas/vitrine").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/marcas/obter").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/premios/obter").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/marcas/obter/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/premios/obter/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/usuarios/sorteio/participar").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/sorteios/historico-sorteio").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/sorteios/historico-sorteio/turma").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
