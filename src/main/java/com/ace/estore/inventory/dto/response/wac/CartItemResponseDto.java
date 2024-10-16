package com.ace.estore.inventory.dto.response.wac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {
	private Integer quantity;
	private Double itemTotal;
	private Boolean available;
	private String name;
	private String brand;
	private Double price;
	private Double discount;
}
