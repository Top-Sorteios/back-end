package br.com.topsorteio.repositories;

import br.com.topsorteio.entities.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface iUserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByEmail(String email);
}
