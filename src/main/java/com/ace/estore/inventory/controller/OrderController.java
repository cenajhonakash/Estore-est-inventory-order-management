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
import org.springframework.web.bind.annotation.RestController;

import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.dto.response.order.OrderResponseDto;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.service.OrderService;

@RestController
@RequestMapping("/v1/customer-order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponseDto> makeOrder(@RequestBody OrderCreateRequestDto orderCreateRequestDto)
			throws ValidationException {
		return new ResponseEntity<OrderResponseDto>(orderService.createOrder(orderCreateRequestDto), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<OrderResponseDto> updateOrder(@RequestBody OrderUpdateRequestDto orderUpdateRequestDto)
			throws ValidationException {
		return new ResponseEntity<OrderResponseDto>(orderService.updateOrder(orderUpdateRequestDto), HttpStatus.OK);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Integer orderId) {
		return new ResponseEntity<OrderResponseDto>(orderService.getOrder(orderId), HttpStatus.OK);
	}
}
