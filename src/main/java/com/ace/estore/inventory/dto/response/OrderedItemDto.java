package com.ace.estore.inventory.dto.response;

import lombok.Builder;

@Builder
public record OrderedItemDto(Integer orderItemId, Integer quantity, Double salePrice, Double discount, String status) {

}
