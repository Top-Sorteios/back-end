package br.com.topsorteio.service;

import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
    public UserModel createUser(UserModel user) {return repository.save(user);}
}
