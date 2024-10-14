package com.ace.estore.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/v1/customer-order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<ApiResponse> makeOrder(@RequestBody OrderCreateRequestDto orderCreateRequestDto)
			throws ValidationException, GeneralException {
		return new ResponseEntity<ApiResponse>(orderService.createOrder(orderCreateRequestDto), HttpStatus.OK);
	}

	@PutMapping("/{order}")
	public ResponseEntity<ApiResponse> updateOrder(@PathVariable Integer order,
			@RequestBody OrderUpdateRequestDto orderUpdateRequestDto)
			throws ValidationException, GeneralException, ResourceNotFoundException {
		return new ResponseEntity<ApiResponse>(orderService.updateOrder(orderUpdateRequestDto, order), HttpStatus.OK);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse> getOrder(@PathVariable Integer orderId)
			throws GeneralException, ResourceNotFoundException, JsonMappingException, JsonProcessingException {
		return new ResponseEntity<ApiResponse>(orderService.getOrder(orderId), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<ApiResponse> getCustomerOrder(@RequestParam String user)
			throws GeneralException, ResourceNotFoundException, JsonMappingException, JsonProcessingException {
		return new ResponseEntity<ApiResponse>(orderService.getCustomerOrder(user), HttpStatus.OK);
	}

	@GetMapping("/item/{item}")
	public ResponseEntity<ApiResponse> getOrdersForItem(@PathVariable Integer item)
			throws GeneralException, ResourceNotFoundException, JsonMappingException, JsonProcessingException {
		return new ResponseEntity<ApiResponse>(orderService.getOrdersForItem(item), HttpStatus.OK);
	}
}
