package com.onlyflans.bakery.persistence;

import com.onlyflans.bakery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IOrderPersistence extends JpaRepository<Order, String> {
}
