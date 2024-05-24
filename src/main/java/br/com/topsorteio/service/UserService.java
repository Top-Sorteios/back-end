package br.com.topsorteio.service;

import br.com.topsorteio.entities.UserModel;
import br.com.topsorteio.exceptions.EventTimeOutException;
import br.com.topsorteio.repositories.iUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
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
        try{
            return repository.findByEmail(email);
        }catch(RuntimeException ex){
            throw new EventTimeOutException();
        }
    }
    public Optional<UserModel> findById(Integer id){
        try{
            return repository.findById(id);
        }catch(RuntimeException ex){
            throw new EventTimeOutException();
        }
    }
    public UserModel createUser(UserModel user) {return repository.save(user);}

    public UserModel update(UserModel user){
        try{
            return repository.save(user);
        }catch (RuntimeException ex){
            throw new EventTimeOutException();
        }
    }
}
