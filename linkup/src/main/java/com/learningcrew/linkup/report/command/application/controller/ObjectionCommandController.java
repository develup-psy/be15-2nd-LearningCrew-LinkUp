package com.learningcrew.linkup.report.command.application.controller;

import com.learningcrew.linkup.report.command.application.dto.request.CommentObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.request.PostObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.request.ReviewObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ObjectionRegisterResponse;
import com.learningcrew.linkup.report.command.application.service.ObjectionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/objections")
@RequiredArgsConstructor
@Tag(name = "이의 제기 ", description = "사용자 및 사업자의 이의제기 신청 API")
public class ObjectionCommandController {

    private final ObjectionCommandService objectionCommandService;

    @PostMapping("/review/{reviewId}")
    @Operation(summary = "장소 후기 제재 이의 신청", description = "사업자가 장소 후기 제재에 대해 이의를 신청합니다.")
    public ResponseEntity<ObjectionRegisterResponse> submitReviewObjection(
            @PathVariable Integer reviewId,
            @RequestBody @Valid ReviewObjectionRequest request
    ) {
        ObjectionRegisterResponse response = objectionCommandService.submitReviewObjection(reviewId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/post/{postId}")
    @Operation(summary = "게시글 제재 이의 신청", description = "사용자가 게시글 제재에 대해 이의를 신청합니다.")
    public ResponseEntity<ObjectionRegisterResponse> submitPostObjection(
            @PathVariable Integer postId,
            @RequestBody @Valid PostObjectionRequest request
    ) {
        ObjectionRegisterResponse response = objectionCommandService.submitPostObjection(postId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment/{commentId}")
    @Operation(summary = "댓글 제재 이의 신청", description = "사용자가 댓글 제재에 대해 이의를 신청합니다.")
    public ResponseEntity<ObjectionRegisterResponse> submitCommentObjection(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentObjectionRequest request
    ) {
        ObjectionRegisterResponse response = objectionCommandService.submitCommentObjection(commentId, request);
        return ResponseEntity.ok(response);
    }
}
