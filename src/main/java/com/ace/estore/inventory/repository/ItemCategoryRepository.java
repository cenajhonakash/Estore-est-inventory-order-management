package com.ace.estore.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.ItemCategory;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Integer> {

	Optional<ItemCategory> findByTitleIgnoreCase(String title);
}
