package com.ace.estore.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.dto.request.inventory.ItemStockRequestDto;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.service.ItemStockService;

@RestController
@RequestMapping("/v1/stock")
public class StockController {

	@Autowired
	private ItemStockService itemStockService;

	@PostMapping
	public ResponseEntity<ItemStockDto> addItemStock(@RequestBody ItemStockRequestDto itemStockRequestDto)
			throws ResourceExistsException, GeneralException, ResourceNotFoundException, ValidationException {
		ItemStockDto dto = itemStockService.addItemStock(itemStockRequestDto);
		return new ResponseEntity<ItemStockDto>(dto,
				HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<ItemStockDto> updateItemStock(
			@RequestBody ItemStockRequestDto itemStockRequestDto)
			throws GeneralException, ResourceNotFoundException, ValidationException {
		return new ResponseEntity<ItemStockDto>(itemStockService.updateItemStock(itemStockRequestDto),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ItemStockDto>> getItemStock(@RequestParam(required = false) Integer itemId,
			@RequestParam(required = false) String store) throws ResourceNotFoundException {
		return new ResponseEntity<List<ItemStockDto>>(itemStockService.getItemStock(itemId, store),
				HttpStatus.OK);
	}
}
