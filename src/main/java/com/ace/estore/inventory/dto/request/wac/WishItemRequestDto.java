package com.ace.estore.inventory.dto.request.wac;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WishItemRequestDto(List<Integer> productId) {

}
