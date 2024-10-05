package com.ace.estore.inventory.service.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.dto.StockUpdateDetailsDto;
import com.ace.estore.inventory.dto.request.inventory.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.inventory.ItemRequestDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.ItemCategory;
import com.ace.estore.inventory.entity.resourcing.ItemStock;
import com.ace.estore.inventory.entity.resourcing.StockUpdateDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class InventoryHelper {

	@Autowired
	private ObjectMapper objectMapper;

	public ItemCategory buildCategoryEntity(CategoryRequestDto categoryDto) {
		return ItemCategory.builder().title(categoryDto.title()).description(categoryDto.description())
				.coverImage(categoryDto.coverImage()).build();
	}

	public ItemCategory updateItemCategory(CategoryRequestDto categoryDto, ItemCategory itemCategory) {
		if (categoryDto.coverImage() != null)
			itemCategory.setCoverImage(categoryDto.coverImage());
		if (categoryDto.description() != null)
			itemCategory.setDescription(categoryDto.description());
		if (categoryDto.title() != null)
			itemCategory.setTitle(categoryDto.title());
		return itemCategory;
	}

	public Item buildItemEntity(ItemRequestDto itemDto, ItemCategory category) {
		return Item.builder().name(itemDto.name()).brand(itemDto.brand()).description(itemDto.description())
				.image(itemDto.imageUrl()).discountPercent(itemDto.discountPercent()).price(itemDto.price())
				.quantity(itemDto.quantity()).category(category).build();
	}

	public Item updateItemDetails(Item item, ItemRequestDto itemDto, ItemCategory itemCategory) {
		if (itemDto.brand() != null)
			item.setBrand(itemDto.brand());
		if (itemDto.description() != null)
			item.setDescription(itemDto.description());
		if (itemDto.discountPercent() != null)
			item.setDiscountPercent(itemDto.discountPercent());
		if (itemDto.name() != null)
			item.setName(itemDto.name());
		if (itemDto.price() != null)
			item.setPrice(itemDto.price());
		if (itemDto.quantity() != null)
			item.setQuantity(itemDto.quantity());
		if (itemDto.imageUrl() != null)
			item.setImage(itemDto.imageUrl());
		if (itemDto.category() != null && itemCategory != null)
			item.setCategory(itemCategory);
		return item;
	}

	public ItemStock buildItemStockEntity(ItemStockDto itemStockDto) {
		return ItemStock.builder().itemId(itemStockDto.itemId()).storeNumber(itemStockDto.storeNumber())
				.stockQuantity(itemStockDto.stockQuantity()).thresholdLimit(itemStockDto.thresholdLimit())
				.updateDetails(itemStockDto.updateDetails() != null && !itemStockDto.updateDetails().isEmpty() ? null
						: buildUpdateEntity(itemStockDto.updateDetails()))
				.build();
	}

	private List<StockUpdateDetails> buildUpdateEntity(List<StockUpdateDetailsDto> updateDetails) {
		return updateDetails.stream().map(dto -> mapper.mapDtoToEntity(dto)).collect(Collectors.toList());
	}

}
