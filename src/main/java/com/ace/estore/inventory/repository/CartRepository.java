package com.ace.estore.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
