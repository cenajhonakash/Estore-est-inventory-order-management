package com.ace.estore.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findAllByUserId(String user);
}
