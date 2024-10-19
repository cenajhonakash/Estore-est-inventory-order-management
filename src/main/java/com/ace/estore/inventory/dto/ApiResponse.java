package com.ace.estore.inventory.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ApiResponse(List<BaseResponseDto> data, FailureResponse failure, String message) {

}
