package com.ace.estore.inventory.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerDetailsDto(String phone, String state, String zipCode, String billingAddress, String email) {

}
