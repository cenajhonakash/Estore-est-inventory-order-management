package com.ace.estore.inventory.repository.resourcing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.estore.inventory.entity.resourcing.ItemStock;
import com.ace.estore.inventory.entity.resourcing.ItemStockId;

public interface ItemStockRepository extends JpaRepository<ItemStock, ItemStockId> {

	List<ItemStock> findAllByItemId(Integer itemId);

	List<ItemStock> findAllByStoreNumber(String store);
}
