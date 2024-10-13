package com.ace.estore.inventory.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@JsonInclude(Include.NON_NULL)
@Builder
public record ItemResponseDto(@JsonAlias("id") Integer itemId, String name, String brand,
		@JsonAlias("details") String description, Double price,
		@JsonAlias("discount") Double discountPercent, Integer quantity, String image, String type) {

}
