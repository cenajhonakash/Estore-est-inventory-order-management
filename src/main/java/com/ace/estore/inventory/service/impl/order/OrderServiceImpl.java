package com.ace.estore.inventory.service.impl.order;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.estore.inventory.constants.OrderStatusEnum;
import com.ace.estore.inventory.dto.request.order.OrderCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderItemCreateRequestDto;
import com.ace.estore.inventory.dto.request.order.OrderUpdateRequestDto;
import com.ace.estore.inventory.dto.response.order.OrderResponseDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.Order;
import com.ace.estore.inventory.entity.OrderItem;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.repository.ItemRepository;
import com.ace.estore.inventory.repository.OrderRepository;
import com.ace.estore.inventory.service.OrderService;
import com.ace.estore.inventory.service.helper.OrderHelper;

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

	@Override
	@Transactional(rollbackOn = Exception.class)
	public OrderResponseDto createOrder(OrderCreateRequestDto orderCreateRequestDto) throws ValidationException {

		helper.validateMandatoryAttributeForNewOrder(orderCreateRequestDto);

		AtomicReference<Order> order = new AtomicReference<>(Order.builder().orderItems(new ArrayList<>()).build());

		boolean anyError = orderCreateRequestDto.products().stream().map(product -> {
			try {
				Item item = itemRepo.findById(product.productId()).orElseThrow(
						() -> new ResourceNotFoundException("No product found with id: " + product.productId()));
				if (item.getQuantity() > product.quantity()) { // fulfill from store
					order.get().getOrderItems().add(fulfillFromStore(item, product));
					// fulfillFromStore(item, product);
				} else {// fulfill from warehouse
					order.get().getOrderItems().add(fulfillFromWareshouse(item, product));
				}
			} catch (ResourceNotFoundException e) {
				log.error("Failure due to: " + e.getMessage());
				return true;
			} catch (Exception e) {
				log.error("Failure due to: " + e.getMessage());
				return true;
			}
			return false;
		}).filter(anyIssue -> anyIssue).findFirst().isPresent();

		if (anyError)
			throw new ValidationException("Error while processing request");
		return Objects.nonNull(order) ? null : null;
	}

	@Override
	public OrderResponseDto updateOrder(OrderUpdateRequestDto orderUpdateRequestDto) throws ValidationException {
		helper.validateMandatoryAttributeForOrderUpdate(orderUpdateRequestDto);
		return null;
	}

	@Override
	public OrderResponseDto getOrder(Integer orderId) {
		return null;
	}

	private OrderItem fulfillFromStore(Item item, OrderItemCreateRequestDto createItemDto) {
		return OrderItem.builder().status(OrderStatusEnum.CREATED.name()).discount(createItemDto.discount()).build();
	}

	private OrderItem fulfillFromWareshouse(Item item, OrderItemCreateRequestDto product) {
		return null;

	}

}
