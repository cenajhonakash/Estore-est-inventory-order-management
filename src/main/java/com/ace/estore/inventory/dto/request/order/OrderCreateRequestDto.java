package com.ace.estore.inventory.dto.request.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderCreateRequestDto(String paymentStatus, String userId, CustomerDetailsDto userDetails,
		List<OrderItemCreateRequestDto> products) {

}
