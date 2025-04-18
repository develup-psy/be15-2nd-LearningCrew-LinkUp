package com.learningcrew.linkup.community.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.community.command.application.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> likePost(@PathVariable int postId,
                                                      @RequestParam int userId) {
        postLikeService.likePost(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlikePost(@PathVariable int postId,
                                                        @RequestParam int userId) {
        postLikeService.unlikePost(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}