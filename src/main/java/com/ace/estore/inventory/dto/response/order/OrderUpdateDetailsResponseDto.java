package com.ace.estore.inventory.dto.response.order;

import com.ace.estore.inventory.dto.CustomerOrderUserDetailsDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record OrderUpdateDetailsResponseDto(Integer quantity, String status, String needDeliveryDate,
		CustomerOrderUserDetailsDto userInfo, String updatedTime) {

}
