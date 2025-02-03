package com.ace.estore.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.inventory.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.inventory.ItemRequestDto;
import com.ace.estore.inventory.dto.response.CategoryResponseDto;
import com.ace.estore.inventory.dto.response.ItemResponseDto;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.service.CategoryAndItemService;

@RestController
@RequestMapping("/v1/inventory")
//@CrossOrigin("*")
public class InventoryItemController {

	@Autowired
	private CategoryAndItemService categoryAndItemService;

	@PostMapping("/category")
	public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody CategoryRequestDto category)
			throws ResourceExistsException {
		return new ResponseEntity<CategoryResponseDto>(categoryAndItemService.addCategory(category), HttpStatus.OK);
	}

	@GetMapping("/category")
	public ResponseEntity<List<CategoryResponseDto>> getCategories() throws ResourceNotFoundException {
		return new ResponseEntity<>(categoryAndItemService.getCategories(), HttpStatus.OK);
	}

	@GetMapping("/category/{id}")
	public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable("id") Integer categoryId)
			throws ResourceNotFoundException {
		return new ResponseEntity<CategoryResponseDto>(categoryAndItemService.getCategory(categoryId), HttpStatus.OK);
	}

	@PutMapping("/category/{id}")
	public ResponseEntity<CategoryResponseDto> updateCategory(@RequestBody CategoryRequestDto category,
			@PathVariable("id") Integer categoryId) throws ResourceNotFoundException, ResourceExistsException {
		return new ResponseEntity<CategoryResponseDto>(categoryAndItemService.updateCategory(category, categoryId),
				HttpStatus.OK);
	}

	@DeleteMapping("/category/{id}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") Integer categoryId)
			throws ResourceNotFoundException {
		return new ResponseEntity<ApiResponse>(categoryAndItemService.deleteCategory(categoryId), HttpStatus.OK);
	}

	@PostMapping("/item")
	public ResponseEntity<ItemResponseDto> addItem(@RequestBody ItemRequestDto itemDto)
			throws ResourceNotFoundException {
		return new ResponseEntity<ItemResponseDto>(categoryAndItemService.addItem(itemDto), HttpStatus.OK);
	}

	@GetMapping("/item/{id}")
	public ResponseEntity<ItemResponseDto> getItem(@PathVariable("id") Integer itemId)
			throws ResourceNotFoundException {
		return new ResponseEntity<ItemResponseDto>(categoryAndItemService.getItem(itemId), HttpStatus.OK);
	}

	@PutMapping("/item/{id}")
	public ResponseEntity<ItemResponseDto> updateItem(@RequestBody ItemRequestDto itemDto,
			@PathVariable("id") Integer itemId) throws ResourceNotFoundException {
		return new ResponseEntity<ItemResponseDto>(categoryAndItemService.updateItem(itemDto, itemId), HttpStatus.OK);
	}

	@DeleteMapping("/item/{id}")
	public ResponseEntity<ApiResponse> deleteItem(@PathVariable("id") Integer itemId) throws ResourceNotFoundException {
		return new ResponseEntity<ApiResponse>(categoryAndItemService.deleteItem(itemId), HttpStatus.OK);
	}
}
