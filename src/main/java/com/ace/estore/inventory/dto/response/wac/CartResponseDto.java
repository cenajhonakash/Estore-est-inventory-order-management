package com.ace.estore.inventory.dto.response.wac;

import java.util.List;

import com.ace.estore.inventory.dto.BaseResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto extends BaseResponseDto {
	private String cartId;
	private String user;
	private Double cartValue;
	List<CartItemResponseDto> products;
}
