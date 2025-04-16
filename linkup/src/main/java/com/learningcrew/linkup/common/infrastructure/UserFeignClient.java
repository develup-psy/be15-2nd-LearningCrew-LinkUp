package com.learningcrew.linkup.common.infrastructure;

import com.learningcrew.linkup.common.dto.query.UserInfoResponse;
import com.learningcrew.linkup.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "linkupuser", path = "/users", configuration = FeignClientConfig.class, fallbackFactory = UserFeignFallback.class)
public interface UserFeignClient {
    @PostMapping("/users/{userId}/point/increase")
    void increasePoint(@PathVariable("userId") int userId, @RequestParam int amount);

    @GetMapping("/users/me/{userId}")
    UserInfoResponse getUserById(@PathVariable("userId") int userId);

    @GetMapping("/users/me/{userId}/email")
    Optional<String> getEmailByUserId(int userId);

    @GetMapping("/users/{userId}/exists")
    Boolean existsUser(@PathVariable("userId") int userId);

    @GetMapping("/users/me/{userId}/userName")
    String getUserNameByUserId(int userId);

    @GetMapping("/me/{userId}/point")
    int getPointBalance(@PathVariable("userId") int userId);

    @PostMapping("/users/{userId}/point/decrease")
    void decreasePoint(@PathVariable int userId, @RequestParam int amount);
}
