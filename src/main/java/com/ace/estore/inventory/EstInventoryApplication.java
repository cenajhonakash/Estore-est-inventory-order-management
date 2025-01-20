package com.ace.estore.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.ace.estore.common.config", "com.ace.estore.inventory" })
public class EstInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstInventoryApplication.class, args);
	}

}
