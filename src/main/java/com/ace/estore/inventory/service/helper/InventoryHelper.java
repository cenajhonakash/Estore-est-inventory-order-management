package com.ace.estore.inventory.service.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.dto.StockUpdateDetailsDto;
import com.ace.estore.inventory.dto.mapper.StockUpdateDetailsMapper;
import com.ace.estore.inventory.dto.request.inventory.CategoryRequestDto;
import com.ace.estore.inventory.dto.request.inventory.ItemRequestDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.ItemCategory;
import com.ace.estore.inventory.entity.resourcing.ItemStock;
import com.ace.estore.inventory.entity.resourcing.StockUpdateDetails;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryHelper {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private StockUpdateDetailsMapper stockMapper;

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

	public void validateMandatoryAttributesForNewStock(ItemStockDto itemStockDto) throws ValidationException {
		List<String> validationFailed = new ArrayList<>();

		if (Objects.isNull(itemStockDto.getUpdateDetail())) {
			validationFailed.add("No information found for stock update.");
			throw new ValidationException("Mandatory Attribute missing: " + validationFailed);
		}

		if (Objects.isNull(itemStockDto.getUpdateDetail().getOrderDetails())
				&& Objects.isNull(itemStockDto.getUpdateDetail().getUpdatedByUser()))
			validationFailed.add("No user or order information found for stock update.");

		if (Objects.nonNull(itemStockDto.getUpdateDetail().getOrderDetails())
				&& (Objects.isNull(itemStockDto.getUpdateDetail().getOrderDetails().orderNo())
				|| Objects.isNull(itemStockDto.getUpdateDetail().getOrderDetails().originalReqQty())
						|| Objects.isNull(itemStockDto.getUpdateDetail().getOrderDetails().sourcedQty())))
			validationFailed.add("No order information found for stock update.");

		if (Objects.isNull(itemStockDto.getItemId()) || Objects.isNull(itemStockDto.getStoreNumber()))
			validationFailed.add("Item or Store Number is not available");

		if (!validationFailed.isEmpty())
			throw new ValidationException("Mandatory Attribute missing: " + validationFailed);
	}

	public ItemStock buildItemStockEntity(ItemStockDto itemStockDto) throws GeneralException {
		return ItemStock.builder().itemId(itemStockDto.getItemId()).storeNumber(itemStockDto.getStoreNumber())
				.stockQuantity(itemStockDto.getStockQuantity()).thresholdLimit(itemStockDto.getThresholdLimit())
				.updateDetails(
						itemStockDto.getOrderUpdateDetails() == null || itemStockDto.getOrderUpdateDetails().isEmpty()
								? null
								: buildUpdateEntity(itemStockDto.getOrderUpdateDetails()))
				.build();
	}

	private List<StockUpdateDetails> buildUpdateEntity(List<StockUpdateDetailsDto> updateDetails)
			throws GeneralException {
		AtomicReference<Boolean> ifAnyFailure = new AtomicReference<>(false);
		List<StockUpdateDetails> stockUpdateList = updateDetails.stream().map(dto -> {
			StockUpdateDetails stockUpdateDetails = stockMapper.dtoToEntity(dto);
			try {
				stockUpdateDetails.setUpdatedForOrder(
						dto.getOrderDetails() == null ? null : objectMapper.writeValueAsString(dto.getOrderDetails()));
			} catch (JsonProcessingException e) {
				log.error("Error while converting stock order details to string for dto: {} due to: {}",
						stockUpdateDetails, e.getMessage());
				ifAnyFailure.set(true);
			}
			return stockUpdateDetails;
		}).collect(Collectors.toList());
		if (ifAnyFailure.get().equals(Boolean.TRUE)) {
			throw new GeneralException("Error while writing order details for item stock");
		}
		return stockUpdateList;
	}

}
