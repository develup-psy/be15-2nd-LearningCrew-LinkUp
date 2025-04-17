package com.learningcrew.linkupuser.client;

import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.config.FeignClientConfig;
import com.learningcrew.linkupuser.query.dto.query.UserCommentDto;
import com.learningcrew.linkupuser.query.dto.query.UserPostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "linkupcommon", path = "/community", configuration = FeignClientConfig.class)
public interface CommunityClient {
    @GetMapping("/user/{userId}/posts")
    ApiResponse<List<UserPostDto>> getPostsByUser(@PathVariable("userId") int userId);

    @GetMapping("/user/{userId}/comments")
    ApiResponse<List<UserCommentDto>> getCommentsByUser(@PathVariable("userId") int userId);
}