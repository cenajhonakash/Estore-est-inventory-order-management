package com.ace.estore.inventory.dto;

import java.util.List;

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
public class ItemStockDto {
	private Integer itemId;
	private String storeNumber;
	private Integer stockQuantity;
	private Integer thresholdLimit;
	private List<StockUpdateDetailsDto> orderUpdateDetails;
}
