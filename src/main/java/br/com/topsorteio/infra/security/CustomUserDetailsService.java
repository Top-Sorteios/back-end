package br.com.topsorteio.infra.security;

import br.com.topsorteio.entities.user.UserModel;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = this.repository.findByEmail(username).orElseThrow(() -> new EventNotFoundException("Usuário não encontrado."));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSenha(), new ArrayList<>());
    }
}
