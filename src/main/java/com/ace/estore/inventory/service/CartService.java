package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.wac.CartItemRequestDto;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;

public interface CartService {

	ApiResponse addItemToCart(String userId, CartItemRequestDto cartRequestDto)
			throws ValidationException, ResourceNotFoundException;

	ApiResponse removeItemFromCart(String userId, Integer cartItem)
			throws ResourceNotFoundException, ValidationException;

	ApiResponse clearCart(String userId) throws ResourceNotFoundException;

	ApiResponse getCartForUser(String userId) throws ResourceNotFoundException;

}
