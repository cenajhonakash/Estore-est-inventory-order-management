package com.ace.estore.inventory.service.helper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ace.estore.inventory.constants.ApplicationConstants;

@Component
public class AppUtils {

	public LocalDateTime convertStringToLocalDateTimeMs(String date) {
		return LocalDateTime.parse(date, ApplicationConstants.DT_FORMATTER_MS);
	}

	public Double calculateDiscountedRate(Double costPrice, Double discount) {
		return costPrice - (costPrice * discount / 100);
	}
}
