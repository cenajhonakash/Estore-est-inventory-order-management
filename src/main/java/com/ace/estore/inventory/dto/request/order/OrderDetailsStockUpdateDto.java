package com.ace.estore.inventory.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDetailsStockUpdateDto(String orderNo, Integer originalReqQty, Integer fulfilledQty) {

}
