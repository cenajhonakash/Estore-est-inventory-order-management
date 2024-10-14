package com.ace.estore.inventory.dto.response.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record OrderItemResponseDto(@JsonAlias(value = "item.name") String name, Integer quantity, Double discount,
		Double actualPrice, Double salePrice, String status, String promisedDeliveryDate, String needDeliveryDate,
		List<OrderUpdateDetailsResponseDto> activity) {

}
