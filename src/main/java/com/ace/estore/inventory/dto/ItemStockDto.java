package com.ace.estore.inventory.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ItemStockDto(Integer itemId, Integer storeNumber, Integer stockQuantity, Integer thresholdLimit,
		List<StockUpdateDetailsDto> updateDetails) {

}
