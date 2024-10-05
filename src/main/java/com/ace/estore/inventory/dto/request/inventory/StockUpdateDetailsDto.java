package com.ace.estore.inventory.dto.request.inventory;

import com.ace.estore.inventory.dto.request.order.OrderDetailsStockUpdateDto;

import lombok.Builder;

@Builder
public record StockUpdateDetailsDto(Integer updatedByUser, OrderDetailsStockUpdateDto forOrder, Integer updatedQuantity,
		Integer newStockValue) {

}
