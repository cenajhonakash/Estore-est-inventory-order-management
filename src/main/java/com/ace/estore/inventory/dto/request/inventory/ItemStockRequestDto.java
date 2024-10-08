package com.ace.estore.inventory.dto.request.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemStockRequestDto(Integer itemId, String storeNumber, Integer thresholdLimit, Boolean enable,
		StockUpdateDetailsRequestDto updateDetail) {

}
