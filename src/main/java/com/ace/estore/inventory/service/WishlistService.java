package com.ace.estore.inventory.service;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.wac.WishItemRequestDto;

public interface WishlistService {
	ApiResponse addItemToWishlist(String userId, WishItemRequestDto cartRequestDto);

	void removeItemFromWishlist(String userId, int cartItem);

	void clearWishlist(String userId);

	ApiResponse getWishlistForUser(String userId);
}
