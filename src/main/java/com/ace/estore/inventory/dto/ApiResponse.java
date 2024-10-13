package com.ace.estore.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ApiResponse(FailureResponse failure, BaseResponseDto data) {

}
