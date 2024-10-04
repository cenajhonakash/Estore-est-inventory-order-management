package com.ace.estore.inventory.service.helper;

import org.springframework.stereotype.Component;

import com.ace.estore.inventory.dto.request.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.ItemRequestDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.ItemCategory;

@Component
public class InventoryHelper {

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

}
