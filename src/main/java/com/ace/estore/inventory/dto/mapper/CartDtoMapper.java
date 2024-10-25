package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.response.wac.CartResponseDto;
import com.ace.estore.inventory.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartDtoMapper {

	CartDtoMapper INSTANCE = Mappers.getMapper(CartDtoMapper.class);

//	@Mapping(target = "id", source = "cartId")
	@Mapping(target = "user", source = "userId")
	CartResponseDto entityToDto(Cart entity);
}
