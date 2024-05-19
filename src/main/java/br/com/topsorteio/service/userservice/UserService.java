package br.com.topsorteio.service.userservice;

import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
