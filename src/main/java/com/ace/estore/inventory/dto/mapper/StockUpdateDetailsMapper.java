package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.StockUpdateDetailsDto;
import com.ace.estore.inventory.entity.resourcing.StockUpdateDetails;

@Mapper(componentModel = "spring")
public interface StockUpdateDetailsMapper {
	StockUpdateDetailsMapper INSTANCE = Mappers.getMapper(StockUpdateDetailsMapper.class);

	StockUpdateDetails dtoToEntity(StockUpdateDetailsDto dto);

	StockUpdateDetailsDto entityToDto(StockUpdateDetails entity);
}
