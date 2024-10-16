package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.response.wac.CartItemResponseDto;
import com.ace.estore.inventory.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemDtoMapper {

	CartItemDtoMapper INSTANCE = Mappers.getMapper(CartItemDtoMapper.class);

	@Mapping(target = "name", source = "item.name")
	@Mapping(target = "brand", source = "item.brand")
	@Mapping(target = "price", source = "item.price")
	@Mapping(target = "discount", source = "item.discountPercent")
	@Mapping(target = "available", source = "requestQuantityNotAvailable")
	CartItemResponseDto entityToDto(CartItem entity);
}
