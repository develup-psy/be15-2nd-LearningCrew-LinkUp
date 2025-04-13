package com.learningcrew.linkup.report.command.application.controller;

import com.learningcrew.linkup.report.command.application.dto.request.PenaltyRequest;
import com.learningcrew.linkup.report.command.application.dto.response.PenaltyResponse;
import com.learningcrew.linkup.report.command.application.service.PenaltyAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/penalty")
@RequiredArgsConstructor
@Tag(name = "사용자 제재 관리", description = "관리자의 사용자 제재 관리 API")
public class PenaltyCommandController {

    private final PenaltyAdminService penaltyAdminService;

    /**
     * 게시글 제재
     */
    @PutMapping("/post/{postId}")
    @Operation(summary = "게시글 제재", description = "관리자가 신고가 누적되거나 서비스의 규칙에 위배되는 게시글을 권한을 통해 비공개 처리한다")
    public ResponseEntity<PenaltyResponse> penalizePost(
            @PathVariable int postId,
            @RequestBody @Valid PenaltyRequest request
    ) {
        // TODO: 게시글 삭제 서비스 연동 시 주석 해제
        // postCommandService.deletePost(postId);

        return ResponseEntity.ok(penaltyAdminService.penalizePost(postId, request));
    }

    /**
     * 댓글 제재
     */
    @PutMapping("/comment/{commentId}")
    @Operation(summary = "댓글 제재", description = "관리자가 신고가 누적되거나 서비스의 규칙에 위배되는 댓글을 권한을 통해 비공개 처리한다")
    public ResponseEntity<PenaltyResponse> penalizeComment(
            @PathVariable Long commentId,
            @RequestBody @Valid PenaltyRequest request
    ) {
        // TODO: 댓글 삭제 서비스 연동 시 주석 해제
        // postCommentCommandService.deletePostComment(BigInteger.valueOf(commentId));

        return ResponseEntity.ok(penaltyAdminService.penalizeComment(commentId, request));
    }

    /**
     * 장소 후기 제재 요청 (검토 상태로 전환)
     */
    @PutMapping("/placeReview/{reviewId}")
    @Operation(summary = "장소 후기 제재 요청", description = "사업자가 이의를 제기한 장소 후기를 검토 상태로 변경한다.")
    public ResponseEntity<PenaltyResponse> penalizePlaceReview(
            @PathVariable int reviewId,
            @RequestBody @Valid PenaltyRequest request
    ) {
        return ResponseEntity.ok(penaltyAdminService.penalizeReview(reviewId, request));
    }

    /**
     * 장소 후기 제재 확정 (비공개 처리)
     */
    @PutMapping("/placeReview/{reviewId}/done")
    @Operation(summary = "장소 후기 제재 확정", description = "사업자가 장소후기에 제기한 이의를 승인하여 해당 장소 후기를 비공개 처리한다.")
    public ResponseEntity<PenaltyResponse> confirmPlaceReviewPenalty(
            @PathVariable int reviewId
    ) {
        return ResponseEntity.ok(penaltyAdminService.confirmReviewPenalty(reviewId));
    }

    /**
     * 제재 철회
     */
    @PutMapping("/{penaltyId}")
    @Operation(summary = "제재 철회", description = "관리자가 이의제기 혹은 추가 정보로 인해 잘못 제재된 컨텐츠에 대한 재제를 철회한다.")
    public ResponseEntity<PenaltyResponse> cancelPenalty(
            @PathVariable long penaltyId
    ) {
        return ResponseEntity.ok(penaltyAdminService.cancelPenalty(penaltyId));
    }
}
