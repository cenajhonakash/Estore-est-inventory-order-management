package com.ace.estore.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record CustomerOrderUserDetailsDto(String phone, String state, String zipCode, String billingAddress,
		String email, String needDeliveryDate) {

}
