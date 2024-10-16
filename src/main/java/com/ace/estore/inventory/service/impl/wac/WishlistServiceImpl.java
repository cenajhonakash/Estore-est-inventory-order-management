package com.ace.estore.inventory.service.impl.wac;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.wac.WishItemRequestDto;
import com.ace.estore.inventory.service.WishlistService;

public class WishlistServiceImpl implements WishlistService {

	@Override
	public ApiResponse addItemToWishlist(String userId, WishItemRequestDto cartRequestDto) {

		return null;
	}

	@Override
	public void removeItemFromWishlist(String userId, int cartItem) {

	}

	@Override
	public void clearWishlist(String userId) {

	}

	@Override
	public ApiResponse getWishlistForUser(String userId) {

		return null;
	}

}
