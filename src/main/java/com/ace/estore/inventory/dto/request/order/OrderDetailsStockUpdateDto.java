package com.ace.estore.inventory.dto.request.order;

import lombok.Builder;

@Builder
public record OrderDetailsStockUpdateDto(String orderNo, Integer originalReqQty, Integer sourcedQty) {

}
