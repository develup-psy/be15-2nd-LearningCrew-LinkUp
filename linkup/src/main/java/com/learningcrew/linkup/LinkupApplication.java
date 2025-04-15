package com.learningcrew.linkup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LinkupApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkupApplication.class, args);
	}

}
