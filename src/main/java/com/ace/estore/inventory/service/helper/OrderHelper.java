package com.ace.estore.inventory.service.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ace.estore.inventory.dto.CustomerOrderUserDetailsDto;
import com.ace.estore.inventory.dto.mapper.OrderItemResponseDtoMapper;
import com.ace.estore.inventory.dto.mapper.OrderResponseDtoMapper;
import com.ace.estore.inventory.dto.mapper.OrderUpdateDetailsResponseDtoMapper;
import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.dto.response.order.OrderResponseDto;
import com.ace.estore.inventory.entity.Order;
import com.ace.estore.inventory.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderHelper {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private OrderResponseDtoMapper orderResponseMapper;
	@Autowired
	private OrderItemResponseDtoMapper orderItemResponseMapper;
	@Autowired
	private OrderUpdateDetailsResponseDtoMapper orderUpdateResponseMapper;

	public void validateMandatoryAttributeForNewOrder(OrderCreateRequestDto orderCreateRequestDto)
			throws ValidationException {
		List<String> validationErrors = new ArrayList<>();
		if (Objects.isNull(orderCreateRequestDto.paymentStatus()))
			validationErrors.add("Missing payment Status");
		if (Objects.isNull(orderCreateRequestDto.userId()))
			validationErrors.add("Missing user id");
		if (Objects.isNull(orderCreateRequestDto.userDetails())
				|| Objects.isNull(orderCreateRequestDto.userDetails().billingAddress())
				|| Objects.isNull(orderCreateRequestDto.userDetails().zipCode())
				|| Objects.isNull(orderCreateRequestDto.userDetails().state())
				|| Objects.isNull(orderCreateRequestDto.userDetails().phone()))
			validationErrors.add("Missing user information for billing & delivery");
		if (Objects.isNull(orderCreateRequestDto.products()) || orderCreateRequestDto.products().isEmpty())
			validationErrors.add("Missing order items");
		if (Objects.isNull(orderCreateRequestDto.paymentStatus()))
			validationErrors.add("No Payment Status");
		if (Objects.isNull(orderCreateRequestDto.paymentStatus()))
			validationErrors.add("No Payment Status");

		if (!validationErrors.isEmpty())
			throw new ValidationException("Validation failed due to: " + validationErrors);
	}

	public void validateMandatoryAttributeForOrderUpdate(OrderUpdateRequestDto orderUpdateRequestDto)
			throws ValidationException {
		List<String> validationErrors = new ArrayList<>();
		if (Objects.isNull(orderUpdateRequestDto.userId()))
			validationErrors.add("Missing user id");
		if (Objects.nonNull(orderUpdateRequestDto.userDetails())) {
			if (Objects.isNull(orderUpdateRequestDto.userDetails().billingAddress())
					&& Objects.isNull(orderUpdateRequestDto.userDetails().zipCode())
					&& Objects.isNull(orderUpdateRequestDto.userDetails().state())
					&& Objects.isNull(orderUpdateRequestDto.userDetails().phone())
					&& Objects.isNull(orderUpdateRequestDto.userDetails().email()))
				validationErrors.add("No user information present for billing update");
		}
		if (Objects.nonNull(orderUpdateRequestDto.products())) {
			if (orderUpdateRequestDto.products().stream().filter(item -> Objects.isNull(item.productId())).findFirst()
					.isPresent())
				validationErrors.add("No product id items to update");
		}
	}

	public OrderResponseDto buildOrderResponse(Order createdOrder)
			throws JsonMappingException, JsonProcessingException {
		OrderResponseDto orderResponseDto = orderResponseMapper.entityToDto(createdOrder);
		orderResponseDto.setItems(createdOrder.getOrderItems().stream()
				.map(item -> orderItemResponseMapper.entityToDto(item)).collect(Collectors.toList()));
		orderResponseDto
				.setUserInfo(objectMapper.readValue(createdOrder.getUserDetails(), CustomerOrderUserDetailsDto.class));
		return orderResponseDto;
	}

}
