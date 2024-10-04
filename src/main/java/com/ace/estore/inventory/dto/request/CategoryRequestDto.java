package com.ace.estore.inventory.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryRequestDto(String title, String description, String coverImage) {

}
