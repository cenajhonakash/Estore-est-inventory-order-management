package com.ace.estore.inventory.dto.response.order;

import java.util.List;

import com.ace.estore.inventory.dto.BaseResponseDto;
import com.ace.estore.inventory.dto.CustomerOrderUserDetailsDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class OrderResponseDto extends BaseResponseDto {
	private Integer orderNumber;
	private String status;
	private String payment;
	private String refund;
	private String orderDate;
	private String userId;
	private CustomerOrderUserDetailsDto userInfo;
	private List<OrderItemResponseDto> items;
}
