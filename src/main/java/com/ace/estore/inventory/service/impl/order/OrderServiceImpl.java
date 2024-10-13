package com.ace.estore.inventory.service.impl.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.estore.inventory.constants.OrderItemStatusEnum;
import com.ace.estore.inventory.constants.OrderStatusEnum;
import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.FailureResponse;
import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderItemCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.dto.response.order.OrderResponseDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.Order;
import com.ace.estore.inventory.entity.OrderItem;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.PriceChangedForProductException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.repository.ItemRepository;
import com.ace.estore.inventory.repository.OrderRepository;
import com.ace.estore.inventory.service.OrderService;
import com.ace.estore.inventory.service.helper.AppUtils;
import com.ace.estore.inventory.service.helper.OrderHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ItemRepository itemRepo;
	@Autowired
	private OrderHelper helper;
	@Autowired
	private AppUtils appUtils;
	@Autowired
	private ObjectMapper mapper;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public ApiResponse createOrder(OrderCreateRequestDto orderCreateRequestDto)
			throws ValidationException, GeneralException {

		helper.validateMandatoryAttributeForNewOrder(orderCreateRequestDto);

		Order order = Order.builder().orderItems(new ArrayList<>()).build();
		List<String> errors = new ArrayList<>();
		List<String> warnings = new ArrayList<>();

		orderCreateRequestDto.products().stream().forEach(product -> {
			try {
				Item item = itemRepo.findById(product.productId()).orElseThrow(
						() -> new ResourceNotFoundException("No product found with id: " + product.productId()));
				if (item.getQuantity() > product.quantity()) { // fulfill from store
					order.getOrderItems().add(fulfillFromStore(item, product));
				} else {// fulfill from warehouse
					order.getOrderItems().add(fulfillFromWareshouse(item, product));
				}
			} catch (ResourceNotFoundException e) {
				log.warn("Failure due to: " + e.getMessage());
				warnings.add(e.getMessage());
			} catch (PriceChangedForProductException e) {
				log.warn("Failure due to: " + e.getMessage());
				warnings.add(e.getMessage());
			}
		});

		if (!warnings.isEmpty()) {
			return ApiResponse.builder().failure(FailureResponse.builder().warnings(warnings).build()).build();
		}
		order.setUserId(orderCreateRequestDto.userId());
		order.setPaymentStatus(orderCreateRequestDto.paymentStatus());
		order.setStatus(OrderStatusEnum.PLACED.name());
		try {
			order.setUserDetails(mapper.writeValueAsString(orderCreateRequestDto.userDetails()));
		} catch (JsonProcessingException e) {
			log.error("Exception writing value");
			throw new GeneralException("Error while writing user details: " + orderCreateRequestDto.userDetails());
		}
		Order createdOrder = orderRepository.save(order);
		OrderResponseDto response = null;
		try {
			response = helper.buildOrderResponse(createdOrder);
		} catch (JsonProcessingException e) {
			/*
			 * This block will send response if any case of failure while converting the
			 * userInfo(billing address etc)
			 */
			log.error("Error while converting to dto due to: {}", e.getMessage());
			response = OrderResponseDto.builder().orderNumber(createdOrder.getOrderId())
					.status(createdOrder.getStatus()).payment(createdOrder.getPaymentStatus()).build();
			errors.add("Cannot convert userInfo, Please ask user to update the address for order: "
					+ createdOrder.getOrderId());
		}
		return ApiResponse.builder().failure(FailureResponse.builder().warnings(warnings).errors(errors).build())
				.data(response).build();
	}

	@Override
	public ApiResponse updateOrder(OrderUpdateRequestDto orderUpdateRequestDto) throws ValidationException {
		helper.validateMandatoryAttributeForOrderUpdate(orderUpdateRequestDto);
		return null;
	}

	@Override
	public ApiResponse getOrder(Integer orderId) {
		return null;
	}

	private OrderItem fulfillFromStore(Item item, OrderItemCreateRequestDto createItemDto)
			throws PriceChangedForProductException {
		if (item.getPrice().compareTo(createItemDto.cost()) != 0) {
			throw new PriceChangedForProductException("Price has been changed for product: " + item.getName());
		}
		item.setQuantity(item.getQuantity() - createItemDto.quantity());
		return OrderItem.builder().status(OrderItemStatusEnum.CREATED.name()).discount(createItemDto.discount())
				.actualPrice(item.getPrice())
				.salePrice(appUtils.calculateDiscountedRate(item.getPrice(), createItemDto.discount()))
				.quantity(createItemDto.quantity())
				.needDeliveryDate(Objects.nonNull(createItemDto.needDelivery())
						? appUtils.convertStringToLocalDateTimeMs(createItemDto.needDelivery())
						: null)
				.promisedDeliveryDate(Objects.nonNull(createItemDto.needDelivery())
						? appUtils.convertStringToLocalDateTimeMs(createItemDto.promisedDeliveryDate())
						: null)
				.item(item).build();// item needs to be validated
	}

	private OrderItem fulfillFromWareshouse(Item item, OrderItemCreateRequestDto product) {
		return null;
	}

}
