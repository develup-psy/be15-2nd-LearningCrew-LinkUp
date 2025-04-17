package com.learningcrew.linkup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.learningcrew.linkup.common") // FeignClient 경로
public class LinkupApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkupApplication.class, args);
	}

}
