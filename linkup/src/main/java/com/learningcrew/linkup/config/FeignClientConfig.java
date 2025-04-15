package com.learningcrew.linkup.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {

            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if(requestAttributes != null) {

                String userId = requestAttributes.getRequest().getHeader("X-User-Id");
                String role = requestAttributes.getRequest().getHeader("X-User-Role");
                requestTemplate.header("X-User-Id", userId);
                requestTemplate.header("X-User-Role", role);
            }
        };
    }
}
