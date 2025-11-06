package com.onlyflans.bakery.persistence;

import com.onlyflans.bakery.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrderDetailPersistence extends JpaRepository<OrderDetail, String> {
}
