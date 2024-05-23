package br.com.topsorteio.service.userservice;

import br.com.topsorteio.entities.user.UserModel;
import br.com.topsorteio.exceptions.EventNotFoundException;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private iUserRepository repository;

    public List<UserModel> findAll(){
        return repository.findAll();
    }
    public Optional<UserModel> findByEmail(String email){
        return repository.findByEmail(email);
    }
    public Optional<UserModel> findById(Integer id){return repository.findById(id);}

    public UserDetails findByLogin(String email) {return repository.findByLogin(email);}

}
