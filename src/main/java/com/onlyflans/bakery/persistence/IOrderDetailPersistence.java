package com.onlyflans.bakery.persistence;

import com.onlyflans.bakery.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailPersistence extends JpaRepository<OrderDetail, String> {
}
