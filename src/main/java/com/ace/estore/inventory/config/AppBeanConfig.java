package com.ace.estore.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppBeanConfig {

	@Bean
	RestTemplate rest() {
		return new RestTemplate();
	}

}
