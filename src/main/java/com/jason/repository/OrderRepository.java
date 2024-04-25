package com.jason.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jason.model.entity.order.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    Page<OrderEntity> findByUserId(String userId, Pageable pageable);
}
