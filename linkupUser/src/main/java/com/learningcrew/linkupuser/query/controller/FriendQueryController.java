package com.learningcrew.linkupuser.query.controller;


import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.query.UserMeetingDto;
import com.learningcrew.linkupuser.query.dto.response.FriendInfoResponse;
import com.learningcrew.linkupuser.query.dto.response.FriendRequestResponse;
import com.learningcrew.linkupuser.query.service.FriendQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequiredArgsConstructor
@RequestMapping("/friends")
@Tag(name = "친구 관리", description = "친구 관계 관련 조회")
public class FriendQueryController {
    private final FriendQueryService friendQueryService;

    /* 친구 목록 조회 */
    @GetMapping()
    @Operation(summary = "친구 목록 조회", description = "현재 로그인한 사용자의 친구 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<FriendInfoResponse>>> getFriendList(@AuthenticationPrincipal String userId) {
        List<FriendInfoResponse> frinedList = friendQueryService.getFriends(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(frinedList));
    }

    /* 친구 신청 목록 조회*/
    @GetMapping("/received")
    @Operation(summary = "받은 친구 요청 조회", description = "내가 받은 친구 요청 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<FriendRequestResponse>>> getReceivedRequests(@AuthenticationPrincipal String userId) {
        log.info("Get received friend requests list for user {}", Integer.parseInt(userId));
        List<FriendRequestResponse> responses = friendQueryService.getReceivedRequests(Integer.parseInt(userId));
        log.info("받은 친구 요청 조회 완료");
        return ResponseEntity.ok(ApiResponse.success(responses, "받은 친구 요청 조회 성공"));
    }

    /* 친구 개설 모임 조회 */
    @GetMapping("/meetings")
    @Operation(summary = "친구가 개설한 모임 조회", description = "사용자의 친구가 개설한 모임 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<UserMeetingDto>>> getMeetingsByFriends(@AuthenticationPrincipal String userId) {
        List<UserMeetingDto> meetings = friendQueryService.findMeetingsCreatedByFriends(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(meetings,"친구 개설 모임에 성공했습니다. "));
    }
}
