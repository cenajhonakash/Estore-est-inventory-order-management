package com.ace.estore.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.wac.CartItemRequestDto;
import com.ace.estore.inventory.dto.request.wac.WishItemRequestDto;

@RestController
@RequestMapping("/v1/wac")
public class WishListAndCartController {

	@PostMapping("/{userId}")
	public ResponseEntity<ApiResponse> addItemToCart(@PathVariable String userId,
			@RequestBody CartItemRequestDto request) {
		return null;

	}

	@PostMapping("/{userId}")
	public ResponseEntity<ApiResponse> addItemToWishlist(@PathVariable String userId,
			@RequestBody WishItemRequestDto request) {
		return null;

	}

	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable String userId, @PathVariable Integer itemId) {
		return null;
	}

	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponse> removeItemFromWishList(@PathVariable String userId,
			@PathVariable Integer itemId) {
		return null;
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId) {

		return null;
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> clearWishList(@PathVariable String userId) {

		return null;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse> getCart(@PathVariable String userId) {
		return null;
	}

	public ResponseEntity<ApiResponse> getWishList(@PathVariable String userId) {
		return null;
	}

}
