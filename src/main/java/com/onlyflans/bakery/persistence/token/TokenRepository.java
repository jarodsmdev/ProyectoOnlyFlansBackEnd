package com.onlyflans.bakery.persistence.token;

import com.onlyflans.bakery.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    List<Token> findAllValidIsFalseOrRevokedIsFalseByUserRut(String id);
}
