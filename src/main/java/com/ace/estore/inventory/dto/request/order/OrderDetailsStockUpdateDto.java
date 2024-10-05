package com.ace.estore.inventory.dto.request.order;

import lombok.Builder;

@Builder
public record OrderDetailsStockUpdateDto(String orderNo, String originalReqQty, String sourcedQty) {

}
