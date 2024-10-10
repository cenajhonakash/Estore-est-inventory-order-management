package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.response.order.OrderItemResponseDto;
import com.ace.estore.inventory.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemResponseDtoMapper {
	OrderItemResponseDtoMapper INSTANCE = Mappers.getMapper(OrderItemResponseDtoMapper.class);

	@Mapping(target = "activity", source = "orderUpdates")
	@Mapping(target = "name", source = "item.name")
	OrderItemResponseDto entityToDto(OrderItem entity);
}
