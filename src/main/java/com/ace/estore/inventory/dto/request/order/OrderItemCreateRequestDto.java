package com.ace.estore.inventory.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderItemCreateRequestDto(Integer productId, Integer quantity, String promisedDeliveryDate, Double cost,
		Double salePrice, Double discount, String needDelivery) {

}
