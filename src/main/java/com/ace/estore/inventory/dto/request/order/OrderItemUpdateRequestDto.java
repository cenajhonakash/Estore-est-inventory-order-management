package com.ace.estore.inventory.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderItemUpdateRequestDto(Integer productId, Integer quantity, String status, String refundStatus,
		String needDelivery) {

}
