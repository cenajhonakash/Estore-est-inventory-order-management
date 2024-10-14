package com.ace.estore.inventory.dto.request.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderUpdateRequestDto(Integer orderNumber, String needDelivery, String userId, Boolean cancel,
		CustomerDetailsDto userDetails, List<OrderItemUpdateRequestDto> products) {
}
