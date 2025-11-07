package com.onlyflans.bakery.persistence;

import com.onlyflans.bakery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserPersistence extends JpaRepository<User, String> {
}
