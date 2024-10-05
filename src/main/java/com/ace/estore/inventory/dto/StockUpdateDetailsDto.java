package com.ace.estore.inventory.dto;

import java.time.LocalDateTime;

import com.ace.estore.inventory.dto.request.order.OrderDetailsStockUpdateDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record StockUpdateDetailsDto(Integer id, Integer updatedByUser, OrderDetailsStockUpdateDto updatedForOrder,
		LocalDateTime updatedDate, Integer updatedQuantity, Integer newStockValue) {

}
