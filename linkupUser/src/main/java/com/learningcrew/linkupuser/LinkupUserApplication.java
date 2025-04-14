package com.learningcrew.linkupuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LinkupUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkupUserApplication.class, args);
    }

}
