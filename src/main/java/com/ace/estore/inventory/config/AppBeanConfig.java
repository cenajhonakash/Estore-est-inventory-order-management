package com.ace.estore.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppBeanConfig {

	@Bean
	RestTemplate rest() {
		return new RestTemplate();
	}

	@Bean
	ObjectMapper mapper() {
		return new ObjectMapper();
	}
}
