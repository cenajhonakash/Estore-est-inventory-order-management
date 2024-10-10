package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.dto.response.order.OrderResponseDto;
import com.ace.estore.inventory.exception.ValidationException;

public interface OrderService {

	OrderResponseDto createOrder(OrderCreateRequestDto orderCreateRequestDto) throws ValidationException;

	OrderResponseDto updateOrder(OrderUpdateRequestDto orderUpdateRequestDto) throws ValidationException;

	OrderResponseDto getOrder(Integer orderId);
}
