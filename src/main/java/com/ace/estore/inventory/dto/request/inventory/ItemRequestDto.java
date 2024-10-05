package com.ace.estore.inventory.dto.request.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemRequestDto(Integer category, String name, String brand, String description, Double price,
		Double discountPercent,
		Integer quantity,
		String imageUrl) {

}
