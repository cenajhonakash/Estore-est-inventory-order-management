package com.ace.estore.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
