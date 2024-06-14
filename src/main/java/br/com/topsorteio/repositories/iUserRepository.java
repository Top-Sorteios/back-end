package br.com.topsorteio.repositories;

import br.com.topsorteio.dtos.ProcedureParticipandoSorteioResponseDTO;
import br.com.topsorteio.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface iUserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByEmail(String email);

}
