package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.entity.resourcing.ItemStock;

@Mapper(componentModel = "spring")
public interface ItemStockDtoMapper {
	ItemStockDtoMapper INSTANCE = Mappers.getMapper(ItemStockDtoMapper.class);

	ItemStock dtoToEntity(ItemStockDto dto);

	ItemStockDto entityToDto(ItemStock entity);

}
