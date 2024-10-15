package com.ace.estore.inventory.dto.request.wac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WishItemRequestDto(Integer productId) {

}
