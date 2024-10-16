package com.ace.estore.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ace.estore.inventory.dto.response.CategoryResponseDto;
import com.ace.estore.inventory.entity.ItemCategory;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {
	CategoryDtoMapper INSTANCE = Mappers.getMapper(CategoryDtoMapper.class);

	@Mapping(target = "id", source = "category.categoryId")
	@Mapping(target = "products", source = "category.items")
	@Mapping(target = "about", source = "category.description")
	@Mapping(target = "name", source = "category.title")
	CategoryResponseDto mapItemCategoryToDto(ItemCategory category);
}
