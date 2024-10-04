package com.ace.estore.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
