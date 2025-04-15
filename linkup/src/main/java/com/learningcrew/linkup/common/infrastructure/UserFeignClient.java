package com.learningcrew.linkup.common.infrastructure;

import com.learningcrew.linkup.common.dto.query.UserInfoResponse;
import com.learningcrew.linkup.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "LINKUPUSER", path = "/users", configuration = FeignClientConfig.class, fallback = UserFeignFallback.class)
public interface UserFeignClient {
    @GetMapping("/users/me/{userId}")
    UserInfoResponse getUserById(@PathVariable("userId") int userId);

    @GetMapping("/users/me/{userId}/email")
    Optional<String> getEmailByUserId(int userId);

    @GetMapping("/users/{userId}/exists")
    Boolean existsUser(@PathVariable("userId") int userId);
}
