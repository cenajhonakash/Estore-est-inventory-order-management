package com.ace.estore.inventory.service.impl.wac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.mapper.CartDtoMapper;
import com.ace.estore.inventory.dto.mapper.CartItemDtoMapper;
import com.ace.estore.inventory.dto.request.wac.CartItemRequestDto;
import com.ace.estore.inventory.dto.response.wac.CartItemResponseDto;
import com.ace.estore.inventory.dto.response.wac.CartResponseDto;
import com.ace.estore.inventory.entity.Cart;
import com.ace.estore.inventory.entity.CartItem;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.repository.CartRepository;
import com.ace.estore.inventory.repository.ItemRepository;
import com.ace.estore.inventory.service.CartService;
import com.ace.estore.inventory.service.helper.AppUtils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private CartDtoMapper cartMapper;
	@Autowired
	private CartItemDtoMapper cartItemMapper;
	@Autowired
	private AppUtils appUtils;

	@Override
	@Transactional
	public ApiResponse addItemToCart(String userId, CartItemRequestDto cartRequestDto)
			throws ResourceNotFoundException, ValidationException {
		if (cartRequestDto.quanity() < 1)
			throw new ValidationException("Quantity is less than or equals 0");

		Item item = itemRepo.findById(cartRequestDto.productId()).orElseThrow(
				() -> new ResourceNotFoundException("No product found with id: " + cartRequestDto.productId()));

		Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
		Cart cart = null;
		if (cartOpt.isEmpty()) {
			log.info("Creating new cart for user: {}", userId);
			cart = Cart.builder().userId(userId)
					.items(new ArrayList<>(
							Arrays.asList(CartItem.builder().item(item).quantity(cartRequestDto.quanity()).build())))
					.build();
		} else {
			cart = cartOpt.get();
			cart.getItems().add(CartItem.builder().item(item).cart(cart).quantity(cartRequestDto.quanity()).build());
		}
		Cart savedCart = cartRepository.save(cart);
		return ApiResponse.builder().data(Arrays.asList(prepareCartResponse(savedCart))).build();
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {

	}

	@Override
	public void clearCart(String userId) {

	}

	@Override
	public ApiResponse getCartForUser(String userId) {

		return null;
	}

	private CartResponseDto prepareCartResponse(Cart savedCart) {
		AtomicReference<Double> cartTotal = new AtomicReference<Double>(0.0);
		List<CartItemResponseDto> products = savedCart.getItems().stream().map(cartItem -> {
			CartItemResponseDto cartItemResponse = cartItemMapper.entityToDto(cartItem);
			Item inventory = cartItem.getItem();
			if (inventory.getQuantity() >= cartItem.getQuantity()) {
				cartItemResponse.setItemTotal(
						appUtils.calculateDiscountedRate(cartItemResponse.getPrice(), cartItemResponse.getDiscount())
								* cartItemResponse.getQuantity());
				cartTotal.set(cartTotal.get() + cartItemResponse.getItemTotal());
			} else {
				cartItemResponse.setAvailable(Boolean.FALSE);
				cartItemResponse.setItemTotal(Double.valueOf(0));
				cartItemResponse.setDiscount(null);
			}
			return cartItemResponse;
		}).collect(Collectors.toList());

		CartResponseDto cartDto = cartMapper.entityToDto(savedCart);
		cartDto.setProducts(products);
		cartDto.setCartValue(cartTotal.get());

		return cartDto;
	}

}
