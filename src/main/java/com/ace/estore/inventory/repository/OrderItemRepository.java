package com.ace.estore.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
