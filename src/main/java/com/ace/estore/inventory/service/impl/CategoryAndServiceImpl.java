package com.ace.estore.inventory.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.estore.inventory.dto.mapper.CategoryResponseDtoMapper;
import com.ace.estore.inventory.dto.mapper.ItemResponseDtoMapper;
import com.ace.estore.inventory.dto.request.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.ItemRequestDto;
import com.ace.estore.inventory.dto.response.CategoryResponseDto;
import com.ace.estore.inventory.dto.response.ItemResponseDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.ItemCategory;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.repository.ItemCategoryRepository;
import com.ace.estore.inventory.repository.ItemRepository;
import com.ace.estore.inventory.service.CategoryAndItemService;
import com.ace.estore.inventory.service.helper.InventoryHelper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryAndServiceImpl implements CategoryAndItemService {

	@Autowired
	private ItemCategoryRepository categoryRepo;
	@Autowired
	private ItemRepository itemRepo;
	@Autowired
	private InventoryHelper helper;

	@Autowired
	private CategoryResponseDtoMapper categoryMapper;
	@Autowired
	private ItemResponseDtoMapper itemMapper;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public CategoryResponseDto addCategory(CategoryRequestDto categoryDto) throws ResourceExistsException {
		Optional<ItemCategory> category = categoryRepo.findByTitleIgnoreCase(categoryDto.title());
		if (category.isPresent()) {
			throw new ResourceExistsException("Category already exists with name: " + categoryDto.title() + " with id: "
					+ category.get().getCategoryId());
		}

		ItemCategory categoryEntity = helper.buildCategoryEntity(categoryDto);
		return categoryMapper.mapItemCategoryToDto(categoryRepo.save(categoryEntity));
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public ItemResponseDto addItem(ItemRequestDto itemDto) throws ResourceNotFoundException {
		ItemCategory category = categoryRepo.findById(itemDto.category())
				.orElseThrow(() -> new ResourceNotFoundException("No Category found with id: " + itemDto.category()));
		Item item = helper.buildItemEntity(itemDto, category);
		Item itemEntity = itemRepo.saveAndFlush(item);
		return itemMapper.mapItemToDto(itemEntity);
	}

	@Override
	public CategoryResponseDto getCategory(Integer categoryId) throws ResourceNotFoundException {
		Optional<ItemCategory> category = categoryRepo.findById(categoryId);
		if (category.isPresent()) {
			return categoryMapper.mapItemCategoryToDto(category.get());
		}
		throw new ResourceNotFoundException("No Category found with id: " + categoryId);
	}

	@Override
	public ItemResponseDto getItem(Integer itemId) throws ResourceNotFoundException {
		Optional<Item> optItem = itemRepo.findById(itemId);
		if (optItem.isPresent()) {
			return itemMapper.mapItemToDto(optItem.get());
		}
		throw new ResourceNotFoundException("No Item found with id: " + itemId);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public CategoryResponseDto updateCategory(CategoryRequestDto categoryDto, Integer categoryId)
			throws ResourceNotFoundException, ResourceExistsException {
		Optional<ItemCategory> category = categoryRepo.findById(categoryId);
		if (category.isEmpty()) {
			throw new ResourceNotFoundException("No Category found with id: " + categoryId);
		}
		ItemCategory itemCategory = category.get();
		if (itemCategory.getTitle().equalsIgnoreCase(categoryDto.title()))
			throw new ResourceExistsException("Category already exists with name: " + categoryDto.title() + " & id: "
					+ itemCategory.getCategoryId());
		ItemCategory categoryEntity = helper.updateItemCategory(categoryDto, itemCategory);
		categoryEntity.setCategoryId(categoryId);
		categoryRepo.save(categoryEntity);
		return categoryMapper.mapItemCategoryToDto(categoryEntity);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public ItemResponseDto updateItem(ItemRequestDto itemDto, Integer itemId) throws ResourceNotFoundException {
		Optional<Item> optItem = itemRepo.findById(itemId);
		if (optItem.isEmpty())
			throw new ResourceNotFoundException("No Item found with id: " + itemId);
		ItemCategory itemCategory = null;
		if (itemDto.category() != null) {
			itemCategory = categoryRepo.findById(itemDto.category()).orElseThrow(
					() -> new ResourceNotFoundException("No Category found with id: " + itemDto.category()));
		}
		Item itemToUpdate = helper.updateItemDetails(optItem.get(), itemDto, itemCategory);
		Item updatedItem = itemRepo.save(itemToUpdate);
		return itemMapper.mapItemToDto(updatedItem);
	}

	@Override
	public String deleteCategory(Integer categoryId) {
		categoryRepo.deleteById(categoryId);
		return "Deleted";
	}

	@Override
	public String deleteItem(Integer itemId) {
		itemRepo.deleteById(itemId);
		return "Deleted";
	}
}
