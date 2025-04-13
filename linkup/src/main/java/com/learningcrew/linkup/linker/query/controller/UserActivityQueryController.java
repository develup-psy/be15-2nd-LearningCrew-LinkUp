package com.learningcrew.linkup.linker.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.linker.query.dto.query.*;
import com.learningcrew.linkup.linker.query.dto.response.UserProfileResponse;
import com.learningcrew.linkup.linker.query.service.CommunityQueryService;
import com.learningcrew.linkup.linker.query.service.MeetingQueryService;
import com.learningcrew.linkup.linker.query.service.PaymentQueryService;
import com.learningcrew.linkup.linker.query.service.UserQueryServiceImpl;
import com.learningcrew.linkup.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
@Schema(name = "유저 활동 조회", description = "유저의 프로필, 게시글, 댓글, 모임, 포인트, 친구 관련 이력을 조회합니다.")
public class UserActivityQueryController {
    private final UserQueryServiceImpl userQueryService;
    private final CommunityQueryService communityQueryService;
    private final MeetingQueryService meetingQueryService;
    private final PaymentQueryService paymentQueryService;

    /* 프로필 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        log.info("프로필 조회 요청: userId={}",
                userDetails.getUserId());
        UserProfileResponse userProfileResponse = userQueryService.getUserProfile(userDetails.getUserId());
        log.info("프로필 조회 성공");
        return ResponseEntity.ok(ApiResponse.success(userProfileResponse));
    }

    /* 작성한 게시글 조회 */
    @GetMapping("/posts")
    @Operation(summary = "작성한 게시글 조회", description = "사용자가 작성한 게시글 목록을 조회합니다.")
    public ResponseEntity<List<UserPostDto>> getUserPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserPostDto> posts = communityQueryService.findPostsByUser(userDetails.getUserId());
        return ResponseEntity.ok(posts);
    }

    /* 작성한 댓글 조회 */
    @GetMapping("/comments")
    @Operation(summary = "작성한 댓글 조회", description = "사용자가 작성한 댓글 목록을 조회합니다.")
    public ResponseEntity<List<UserCommentDto>> getUserComments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserCommentDto> comments = communityQueryService.findCommentsByUser(userDetails.getUserId());
        return ResponseEntity.ok(comments);
    }

    /* 참여 모임 이력 조회 */
    @GetMapping("/meetings")
    @Operation(summary = "참여한 모임 이력 조회", description = "사용자가 참여한 모임 목록을 조회합니다.")
    public ResponseEntity<List<UserMeetingHistoryDto>> getUserMeetingHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserMeetingHistoryDto> meetings = meetingQueryService.findMeetingsByUser(userDetails.getUserId());
        return ResponseEntity.ok(meetings);
    }

    /* 포인트 조회 */
    @GetMapping("/point")
    @Operation(summary = "포인트 조회", description = "사용자의 현재 포인트를 조회합니다.")
    public ResponseEntity<UserPointDto> getUserPoints(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserPointDto point = paymentQueryService.findUserPoint(userDetails.getUserId());
        return ResponseEntity.ok(point);
    }


    /* 매너 온도 조회 */
    @GetMapping("/manner")
    @Operation(summary = "매너온도 조회", description = "사용자의 매너온도를 조회합니다.")
    public ResponseEntity<UserMannerTemperatureDto> getMannerTemperature(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserMannerTemperatureDto manner = communityQueryService.getMannerTemperature(userDetails.getUserId());
        return ResponseEntity.ok(manner);
    }

}
