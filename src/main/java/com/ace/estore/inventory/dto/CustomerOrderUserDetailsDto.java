package com.ace.estore.inventory.dto;

import lombok.Builder;

@Builder
public record CustomerOrderUserDetailsDto(String phone, String state, String zipCode, String billingAddress,
		String email, String needDeliveryDate) {

}
