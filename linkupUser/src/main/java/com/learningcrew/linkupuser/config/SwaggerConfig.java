package com.learningcrew.linkupuser.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    /* open API 3 스펙에 따라 Swagger 문서 정보를 등록하는 빈 */
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(swaggerInfo());
    }
    private Info swaggerInfo() {
        return new Info()
                .title("Linkup API") //서비스명
                .description("운동 매칭 플랫폼 Linkup의 REST API 명세입니다.")
                .version("1.0.0");
    }
}
