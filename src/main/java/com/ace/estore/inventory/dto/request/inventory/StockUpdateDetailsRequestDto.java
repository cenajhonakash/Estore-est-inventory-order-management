package com.ace.estore.inventory.dto.request.inventory;

import com.ace.estore.inventory.dto.request.order.OrderDetailsStockUpdateDto;

import lombok.Builder;

@Builder
public record StockUpdateDetailsRequestDto(Integer credit, Integer debit, Integer updatedByUser,
		OrderDetailsStockUpdateDto orderDetails) {

}
