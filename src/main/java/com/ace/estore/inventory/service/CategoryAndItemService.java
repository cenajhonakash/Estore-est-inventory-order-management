package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.request.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.ItemRequestDto;
import com.ace.estore.inventory.dto.response.CategoryResponseDto;
import com.ace.estore.inventory.dto.response.ItemResponseDto;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;

public interface CategoryAndItemService {

	CategoryResponseDto addCategory(CategoryRequestDto category) throws ResourceExistsException;

	ItemResponseDto addItem(ItemRequestDto item) throws ResourceNotFoundException;

	CategoryResponseDto getCategory(Integer categoryId) throws ResourceNotFoundException;

	ItemResponseDto getItem(Integer itemId) throws ResourceNotFoundException;

	CategoryResponseDto updateCategory(CategoryRequestDto category, Integer categoryId)
			throws ResourceNotFoundException, ResourceExistsException;

	ItemResponseDto updateItem(ItemRequestDto item, Integer itemId) throws ResourceNotFoundException;

	String deleteCategory(Integer categoryId);

	String deleteItem(Integer itemId);
}
