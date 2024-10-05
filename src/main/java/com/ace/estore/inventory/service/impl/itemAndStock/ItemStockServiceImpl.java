package com.ace.estore.inventory.service.impl.itemAndStock;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.entity.resourcing.ItemStock;
import com.ace.estore.inventory.entity.resourcing.ItemStockId;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.repository.resourcing.ItemStockRepository;
import com.ace.estore.inventory.service.ItemStockService;
import com.ace.estore.inventory.service.helper.InventoryHelper;

@Service
public class ItemStockServiceImpl implements ItemStockService {

	@Autowired
	private ItemStockRepository itemStockRepository;
	@Autowired
	private InventoryHelper helper;

	@Override
	public ItemStockDto addItemStock(ItemStockDto ItemStockDto) throws ResourceExistsException {
		Optional<ItemStock> itemStock = itemStockRepository
				.findById(new ItemStockId(ItemStockDto.itemId(), ItemStockDto.storeNumber()));
		if (itemStock.isPresent()) {
			throw new ResourceExistsException("Stock details already existing for store: " + ItemStockDto.storeNumber()
					+ " & item: " + ItemStockDto.itemId());
		}
		// helper
		ItemStock stock = itemStockRepository.save(helper.buildItemStockEntity(ItemStockDto));
		return null;
	}

	@Override
	public ItemStockDto updateItemStock(ItemStockDto ItemStockDto, Integer itemId, Integer store) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStockDto getItemStock(Integer itemId, Integer storeNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
