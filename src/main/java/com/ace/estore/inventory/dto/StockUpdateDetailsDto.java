package com.ace.estore.inventory.dto;

import com.ace.estore.inventory.dto.request.order.OrderDetailsStockUpdateDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockUpdateDetailsDto {
	private Integer id;
	private Integer updatedByUser;// req & rs
	private String updatedDate;
	private Integer credit;
	private Integer debit;
	private Integer newStockValue;
	private OrderDetailsStockUpdateDto orderDetails; // req & res
}