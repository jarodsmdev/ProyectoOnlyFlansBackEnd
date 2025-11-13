package com.onlyflans.bakery.persistence;

import com.onlyflans.bakery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserPersistence extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    //Optional<User> findByName(String userEmail); //Username para SpringSecurity corresponde al email
}
