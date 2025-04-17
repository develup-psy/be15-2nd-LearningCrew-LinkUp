package com.learningcrew.linkup.community.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.dto.query.UserCommentDto;
import com.learningcrew.linkup.common.dto.query.UserPostDto;
import com.learningcrew.linkup.community.query.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityQueryController {

    private final CommunityService communityService;

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<ApiResponse<List<UserPostDto>>> getPostsByUser(@PathVariable int userId) {
        List<UserPostDto> posts = communityService.findPostsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/user/{userId}/comments")
    public ResponseEntity<ApiResponse<List<UserCommentDto>>> getCommentsByUser(@PathVariable int userId) {
        List<UserCommentDto> comments = communityService.findCommentsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }
}
