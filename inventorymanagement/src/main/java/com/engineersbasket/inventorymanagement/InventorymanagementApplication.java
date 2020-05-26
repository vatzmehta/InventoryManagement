package com.engineersbasket.inventorymanagement;

import com.engineersbasket.inventorymanagement.utils.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties({
//		FileStorageProperties.class
//})

public class InventorymanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorymanagementApplication.class, args);
	}

}
