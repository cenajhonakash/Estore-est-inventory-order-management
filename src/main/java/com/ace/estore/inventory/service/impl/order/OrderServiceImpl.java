package com.ace.estore.inventory.service.impl.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.estore.inventory.constants.OrderItemStatusEnum;
import com.ace.estore.inventory.constants.OrderPaymentEnum;
import com.ace.estore.inventory.constants.OrderStatusEnum;
import com.ace.estore.inventory.dto.ApiResponse;
import com.ace.estore.inventory.dto.BaseResponseDto;
import com.ace.estore.inventory.dto.FailureResponse;
import com.ace.estore.inventory.dto.request.order.CustomerDetailsDto;
import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderItemCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.dto.response.order.OrderResponseDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.Order;
import com.ace.estore.inventory.entity.OrderItem;
import com.ace.estore.inventory.entity.OrderUpdateDetails;
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
import com.fasterxml.jackson.databind.JsonMappingException;
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
				if (item.getQuantity() >= product.quantity()) { // fulfill from store
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
			log.error("Exception writing user details: " + orderCreateRequestDto.userDetails());
			throw new GeneralException("Error while writing user details: " + orderCreateRequestDto.userDetails());
		}
		Order createdOrder = orderRepository.save(order);
		OrderResponseDto response = null;
		try {
			response = helper.buildOrderResponse(createdOrder);
			response.setUserInfo(null); // setting user info as null due to PI level data
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
		return ApiResponse.builder().data(Arrays.asList(response)).build();
	}

	@Override
	public ApiResponse updateOrder(OrderUpdateRequestDto orderUpdateRequestDto, Integer orderNumber)
			throws ValidationException, ResourceNotFoundException {
		helper.validateMandatoryAttributeForOrderUpdate(orderUpdateRequestDto);

		Order order = orderRepository.findById(orderNumber)
				.orElseThrow(() -> new ResourceNotFoundException("No order found with orderId: " + orderNumber));
		if (order.getStatus().equalsIgnoreCase(OrderItemStatusEnum.CANCELLED.name()))
			return ApiResponse.builder().failure(FailureResponse.builder()
					.warnings(Arrays.asList("Cannot update order as it is already cancelled")).build()).build();
		boolean anySD = order.getOrderItems().stream()
				.filter(item -> item.getStatus().equalsIgnoreCase(OrderItemStatusEnum.SHIPPED.name())
						|| item.getStatus().equalsIgnoreCase(OrderItemStatusEnum.DELIVERED.name()))
				.findFirst().isPresent();
		if (anySD)
			return ApiResponse.builder()
					.failure(FailureResponse.builder()
							.warnings(
									Arrays.asList("Cannot update order as few items are already shipped or delivered"))
							.build())
					.build();

		List<String> warnings = new ArrayList<>();
		List<String> errors = new ArrayList<>();

		if (Objects.nonNull(orderUpdateRequestDto.cancel()) && orderUpdateRequestDto.cancel()) {
			order.setStatus(OrderStatusEnum.CANCELLED.name());
			order.getOrderItems().stream().forEach(item -> {
				item.setStatus(OrderItemStatusEnum.CANCELLED.name());
				item.getOrderUpdates()
						.add(OrderUpdateDetails.builder().status(OrderItemStatusEnum.CANCELLED.name()).build());
			});
			order.setRefundStatus(OrderPaymentEnum.REFUND_INTITATED.name());
		} else {
			if (Objects.nonNull(orderUpdateRequestDto.needDelivery())) {
				order.getOrderItems().stream().forEach(item -> {
					LocalDateTime newDeliveryDate = appUtils
							.convertStringToLocalDateTimeMs(orderUpdateRequestDto.needDelivery());
					if (newDeliveryDate.getDayOfYear() > item.getPromisedDeliveryDate().getDayOfYear()) {
						item.setNeedDeliveryDate(newDeliveryDate);
						item.getOrderUpdates()
								.add(OrderUpdateDetails.builder().needDeliveryDate(newDeliveryDate).build());
					}
				});
			} else if (Objects.nonNull(orderUpdateRequestDto.userDetails()))
				try {
					CustomerDetailsDto newCustomerDetails = orderUpdateRequestDto.userDetails();
					validateUpdateForCustomerDetails(newCustomerDetails, warnings);
					if (warnings.isEmpty()) {
						CustomerDetailsDto updatedCustomerDetailsDto = CustomerDetailsDto.builder()
								.billingAddress(newCustomerDetails.billingAddress()).email(newCustomerDetails.email())
								.phone(newCustomerDetails.phone()).state(newCustomerDetails.state())
								.zipCode(newCustomerDetails.zipCode()).build();
						String userInfo = mapper.writeValueAsString(updatedCustomerDetailsDto);
						order.setUserDetails(userInfo);
						order.getOrderItems().stream().forEach(orderItem -> orderItem.getOrderUpdates()
								.add(OrderUpdateDetails.builder().userDetails(userInfo).build()));

					}
				} catch (JsonProcessingException e) {
					warnings.add("Error while converting user details: " + order.getUserDetails());
				}
		}
		if (warnings.isEmpty()) {
			Order updatedOrder = orderRepository.save(order);
			try {
				OrderResponseDto orderResponseDto = helper.buildOrderResponse(updatedOrder);
				return ApiResponse.builder().failure(FailureResponse.builder().build())
						.data(Arrays.asList(orderResponseDto)).build();
			} catch (JsonProcessingException e) {
				errors.add("Error while converting user info " + updatedOrder.getUserDetails());
				log.error("Error while converting to dto due to: {}", e.getMessage());
			}
		}
		return ApiResponse.builder().failure(FailureResponse.builder().warnings(warnings).errors(errors).build())
				.build();
	}

	@Override
	public ApiResponse getOrder(Integer orderId)
			throws ResourceNotFoundException, JsonMappingException, JsonProcessingException {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("No order with id: " + orderId));
		OrderResponseDto orderResponseDto = helper.buildOrderResponse(order);
		return ApiResponse.builder().data(Arrays.asList(orderResponseDto)).build();
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

	@Override
	public ApiResponse getCustomerOrder(String user) throws ResourceNotFoundException {
		List<Order> orders = orderRepository.findAllByUserId(user);
		List<String> error = new ArrayList<>();
		List<BaseResponseDto> orderResponse = orders.stream().map(order -> {
			try {
				return helper.buildOrderResponse(order);
			} catch (JsonProcessingException e) {
				error.add(
						"Cannot convert userInfo for order " + order.getOrderId() + " due to message" + e.getMessage());
				return null;
			}
		}).filter(order -> Objects.nonNull(order)).collect(Collectors.toList());
		return ApiResponse.builder().data(orderResponse).failure(FailureResponse.builder().errors(error).build())
				.build();
	}

	@Override
	public ApiResponse getOrdersForItem(Integer item) {
		// TODO Auto-generated method stub
		return null;
	}

	private void validateUpdateForCustomerDetails(CustomerDetailsDto newCustomerDetails, List<String> warnings) {
		if (Objects.isNull(newCustomerDetails.billingAddress()))
			warnings.add("Invalid billing address");
		if (Objects.isNull(newCustomerDetails.phone()))
			warnings.add("Invalid contact number");
		if (Objects.isNull(newCustomerDetails.state()))
			warnings.add("Invalid state");
		if (Objects.isNull(newCustomerDetails.zipCode()))
			warnings.add("Invalid zip code");
		if (Objects.isNull(newCustomerDetails.email()))
			warnings.add("Invalid email");
	}
}
