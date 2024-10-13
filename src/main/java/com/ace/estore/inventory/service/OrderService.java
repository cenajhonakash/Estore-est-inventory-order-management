package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.ValidationException;

public interface OrderService {

	ApiResponse createOrder(OrderCreateRequestDto orderCreateRequestDto) throws ValidationException, GeneralException;

	ApiResponse updateOrder(OrderUpdateRequestDto orderUpdateRequestDto) throws ValidationException, GeneralException;

	ApiResponse getOrder(Integer orderId) throws GeneralException;
}
