package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.response.ItemResponseDto;
import com.ace.estore.inventory.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemDtoMapper {
	ItemDtoMapper INSTANCE = Mappers.getMapper(ItemDtoMapper.class);

	@Mapping(target = "type", source = "item.category.title")
	ItemResponseDto mapItemToDto(Item item);

}
