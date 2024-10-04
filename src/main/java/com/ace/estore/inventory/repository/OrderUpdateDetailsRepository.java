package com.ace.estore.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.OrderUpdateDetails;

public interface OrderUpdateDetailsRepository extends JpaRepository<OrderUpdateDetails, Integer> {

}
