package com.ace.estore.inventory.dto.request.inventory;

import lombok.Builder;

@Builder
public record ItemStockRequestDto(Integer itemId, String storeNumber, Integer thresholdLimit,
		StockUpdateDetailsRequestDto updateDetail) {

}
