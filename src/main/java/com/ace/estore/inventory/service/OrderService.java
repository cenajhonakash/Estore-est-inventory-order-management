package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface OrderService {

	ApiResponse createOrder(OrderCreateRequestDto orderCreateRequestDto) throws ValidationException, GeneralException;

	ApiResponse updateOrder(OrderUpdateRequestDto orderUpdateRequestDto, Integer order)
			throws ValidationException, ResourceNotFoundException, GeneralException;

	ApiResponse getOrder(Integer orderId)
			throws ResourceNotFoundException, JsonMappingException, JsonProcessingException;

	ApiResponse getCustomerOrder(String user) throws ResourceNotFoundException;

	ApiResponse getOrdersForItem(Integer item);

}
