package com.ace.estore.inventory.service.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ace.estore.inventory.dto.request.inventory.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.inventory.ItemRequestDto;
import com.ace.estore.inventory.dto.request.inventory.ItemStockRequestDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.ItemCategory;
import com.ace.estore.inventory.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryHelper {

	public ItemCategory buildCategoryEntity(CategoryRequestDto categoryDto) {
		return ItemCategory.builder().title(categoryDto.title()).description(categoryDto.description())
				.coverImage(categoryDto.coverImage()).build();
	}

	public ItemCategory updateItemCategory(CategoryRequestDto categoryDto, ItemCategory itemCategory) {
		if (StringUtils.hasLength(categoryDto.coverImage()))
			itemCategory.setCoverImage(categoryDto.coverImage());
		if (StringUtils.hasLength(categoryDto.description()))
			itemCategory.setDescription(categoryDto.description());
		if (StringUtils.hasLength(categoryDto.title()))
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

	public void validateMandatoryAttributesForNewStock(ItemStockRequestDto itemStockDto) throws ValidationException {
		List<String> validationFailed = new ArrayList<>();

		if (Objects.isNull(itemStockDto.updateDetail())) {
			validationFailed.add("No information found for stock update.");
			throw new ValidationException("Mandatory Attribute missing: " + validationFailed);
		}

		if (Objects.isNull(itemStockDto.updateDetail().orderDetails())
				&& Objects.isNull(itemStockDto.updateDetail().updatedByUser()))
			validationFailed.add("No user or order information found for stock update.");

		if (Objects.nonNull(itemStockDto.updateDetail().orderDetails())) {
			if (Objects.isNull(itemStockDto.updateDetail().orderDetails().orderNo())
					|| Objects.isNull(itemStockDto.updateDetail().orderDetails().originalReqQty())
					|| Objects.isNull(itemStockDto.updateDetail().orderDetails().fulfilledQty()))
				validationFailed.add("No order information found for stock update.");
		}

		if (Objects.isNull(itemStockDto.itemId()) || Objects.isNull(itemStockDto.storeNumber()))
			validationFailed.add("Item or Store Number is not available");

		if (Objects.nonNull(itemStockDto.updateDetail().updatedByUser())) {
			if (Objects.isNull(itemStockDto.updateDetail().credit())
					&& Objects.isNull(itemStockDto.updateDetail().debit()) && Objects.isNull(itemStockDto.enable()))
				validationFailed.add("Missing credit/debit information or enable stock");
		}
		if (!validationFailed.isEmpty())
			throw new ValidationException("Mandatory Attribute missing: " + validationFailed);
	}

}
