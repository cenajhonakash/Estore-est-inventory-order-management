package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.exception.ResourceExistsException;

public interface ItemStockService {

	ItemStockDto addItemStock(ItemStockDto ItemStockDto) throws ResourceExistsException;

	ItemStockDto updateItemStock(ItemStockDto ItemStockDto, Integer itemId, Integer store);

	ItemStockDto getItemStock(Integer itemId, Integer storeNumber);
}
