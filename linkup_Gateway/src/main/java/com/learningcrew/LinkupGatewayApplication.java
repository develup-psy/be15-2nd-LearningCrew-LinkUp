package com.learningcrew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LinkupGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkupGatewayApplication.class, args);
    }

}
