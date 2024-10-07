package com.ace.estore.inventory.service;

import java.util.List;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;

public interface ItemStockService {

	ItemStockDto addItemStock(ItemStockDto ItemStockDto)
			throws ResourceExistsException, ResourceNotFoundException, ValidationException, GeneralException;

	ItemStockDto updateItemStock(ItemStockDto ItemStockDto, Integer itemId, String store)
			throws ResourceNotFoundException, GeneralException;

	List<ItemStockDto> getItemStock(Integer itemId, String storeNumber) throws ResourceExistsException;
}
