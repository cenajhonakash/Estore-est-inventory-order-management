package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.wac.CartItemRequestDto;
import com.ace.estore.inventory.exception.ResourceNotFoundException;

public interface CartService {

	ApiResponse addItemToCart(String userId, CartItemRequestDto cartRequestDto) throws ResourceNotFoundException;

	void removeItemFromCart(String userId, int cartItem);

	void clearCart(String userId);

	ApiResponse getCartForUser(String userId);

}
