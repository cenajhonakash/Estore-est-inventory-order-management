package com.ace.estore.inventory.dto.response.order;

import java.util.List;

import com.ace.estore.inventory.dto.CustomerOrderUserDetailsDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record OrderResponseDto(Integer orderNumber, String status, String payment, String refund, String orderDate,
		String userId, CustomerOrderUserDetailsDto userInfo, List<OrderItemResponseDto> orderItems) {

}
