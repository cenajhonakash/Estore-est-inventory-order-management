package com.ace.estore.inventory.service;

import java.util.List;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.inventory.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.inventory.ItemRequestDto;
import com.ace.estore.inventory.dto.response.CategoryResponseDto;
import com.ace.estore.inventory.dto.response.ItemResponseDto;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;

public interface CategoryAndItemService {

	CategoryResponseDto addCategory(CategoryRequestDto category) throws ResourceExistsException;

	ItemResponseDto addItem(ItemRequestDto item) throws ResourceNotFoundException;

	CategoryResponseDto getCategory(Integer categoryId) throws ResourceNotFoundException;

	List<CategoryResponseDto> getCategories() throws ResourceNotFoundException;

	ItemResponseDto getItem(Integer itemId) throws ResourceNotFoundException;

	CategoryResponseDto updateCategory(CategoryRequestDto category, Integer categoryId)
			throws ResourceNotFoundException, ResourceExistsException;

	ItemResponseDto updateItem(ItemRequestDto item, Integer itemId) throws ResourceNotFoundException;

	ApiResponse deleteCategory(Integer categoryId);

	ApiResponse deleteItem(Integer itemId);
}
