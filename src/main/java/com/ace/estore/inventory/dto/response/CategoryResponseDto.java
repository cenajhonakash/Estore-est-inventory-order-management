package com.ace.estore.inventory.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record CategoryResponseDto(String id, String name, String about,
		String coverImage,
		List<ItemResponseDto> products) {
}
