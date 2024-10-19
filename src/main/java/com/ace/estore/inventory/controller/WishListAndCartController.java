package com.ace.estore.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.request.wac.CartItemRequestDto;
import com.ace.estore.inventory.dto.request.wac.WishItemRequestDto;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.service.CartService;

@RestController
@RequestMapping("/v1/wac")
public class WishListAndCartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/{userId}")
	public ResponseEntity<ApiResponse> addItemToCart(@PathVariable String userId,
			@RequestBody CartItemRequestDto cartItemRequest) throws ResourceNotFoundException, ValidationException {
		return new ResponseEntity<ApiResponse>(cartService.addItemToCart(userId, cartItemRequest), HttpStatus.OK);
	}

	@PostMapping("/wl/{userId}")
	public ResponseEntity<ApiResponse> addItemToWishlist(@PathVariable String userId,
			@RequestBody WishItemRequestDto request) {
		return null;

	}

	@PutMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable String userId, @PathVariable Integer itemId)
			throws ResourceNotFoundException, ValidationException {
		return new ResponseEntity<ApiResponse>(cartService.removeItemFromCart(userId, itemId), HttpStatus.OK);
	}

	@PutMapping("/wl/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponse> removeItemFromWishList(@PathVariable String userId,
			@PathVariable Integer itemId) {
		return null;
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId) throws ResourceNotFoundException {
		return new ResponseEntity<ApiResponse>(cartService.clearCart(userId), HttpStatus.OK);
	}

	@DeleteMapping("/wl/{userId}")
	public ResponseEntity<ApiResponse> clearWishList(@PathVariable String userId) {

		return null;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse> getCart(@PathVariable String userId) throws ResourceNotFoundException {
		return new ResponseEntity<ApiResponse>(cartService.getCartForUser(userId), HttpStatus.OK);
	}

	@GetMapping("/wl/{userId}")
	public ResponseEntity<ApiResponse> getWishList(@PathVariable String userId) {
		return null;
	}

}
