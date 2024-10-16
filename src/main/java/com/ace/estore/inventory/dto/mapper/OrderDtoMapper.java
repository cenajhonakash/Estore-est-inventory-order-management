package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.response.order.OrderResponseDto;
import com.ace.estore.inventory.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

	OrderDtoMapper INSTANCE = Mappers.getMapper(OrderDtoMapper.class);

	@Mapping(target = "orderNumber", source = "orderId")
	@Mapping(target = "payment", source = "paymentStatus")
	@Mapping(target = "refund", source = "refundStatus")
	@Mapping(target = "orderDate", source = "createdDate")
	OrderResponseDto entityToDto(Order order);
}
