package com.ace.estore.inventory.dto.response.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record OrderItemResponseDto(String name, Integer quantity, Double discount, Double price, String status,
		String promisedDeliveryDate, String needDeliveryDate, List<OrderUpdateDetailsResponseDto> activity) {

}
