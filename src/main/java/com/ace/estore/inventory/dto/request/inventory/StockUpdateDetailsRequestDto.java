package com.ace.estore.inventory.dto.request.inventory;

import com.ace.estore.inventory.dto.request.order.OrderDetailsStockUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record StockUpdateDetailsRequestDto(Integer credit, Integer debit, Integer updatedByUser,
		OrderDetailsStockUpdateDto orderDetails) {

}
