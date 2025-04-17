package com.learningcrew.linkupuser.client;

import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "common-service", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/users/{userId}/grade")
    ApiResponse<String> getUserGrade(@PathVariable("userId") Long userId);
}
