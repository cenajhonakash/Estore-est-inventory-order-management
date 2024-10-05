package com.ace.estore.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.service.ItemStockService;

@RestController
@RequestMapping("/v1/stock")
public class StockController {

	@Autowired
	private ItemStockService itemStockService;

	@PostMapping
	public ResponseEntity<ItemStockDto> addItemStock(@RequestBody ItemStockDto itemStockRequestDto)
			throws ResourceExistsException {
		return new ResponseEntity<ItemStockDto>(itemStockService.addItemStock(itemStockRequestDto),
				HttpStatus.OK);
	}

	@PutMapping("/{itemId}/{store}")
	public ResponseEntity<ItemStockDto> updateItemStock(@PathVariable Integer itemId, @PathVariable Integer store,
			@RequestBody ItemStockDto itemStockRequestDto) {
		return new ResponseEntity<ItemStockDto>(itemStockService.updateItemStock(itemStockRequestDto, itemId, store),
				HttpStatus.OK);
	}

	@GetMapping("/{itemId}")
	public ResponseEntity<ItemStockDto> getItemStock(@PathVariable Integer itemId,
			@RequestParam Integer store) {
		return new ResponseEntity<ItemStockDto>(itemStockService.getItemStock(itemId, store),
				HttpStatus.OK);
	}
}
