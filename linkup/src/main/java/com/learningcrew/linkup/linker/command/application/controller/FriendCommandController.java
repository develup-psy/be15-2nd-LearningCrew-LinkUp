package com.learningcrew.linkup.linker.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.linker.command.application.service.FriendCommandService;
import com.learningcrew.linkup.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendCommandController {

    private final FriendCommandService friendCommandService;

    /* 친구 요청 신청 */
    @PostMapping("/{targetMemberId}")
    @Operation(summary = "친구 요청 신청", description = "특정 회원에게 친구 요청을 보냅니다.")
    public ResponseEntity<ApiResponse<Void>> sendFriendRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable int targetMemberId){
        log.info("친구 요청 신청 requestId: {}, addressId: {}", customUserDetails.getUserId(), targetMemberId);
        friendCommandService.sendFriendRequest(customUserDetails.getUserId(), targetMemberId);
        log.info("친구 요청 신청에 성공했습니다. ");
        return ResponseEntity.ok(ApiResponse.success(null, "친구 요청이 신청되었습니다."));
    }


    /* 친구 요청 수락 */
    @PutMapping("/{targetMemberId}/accept")
    @Operation(summary = "친구 요청 수락", description = "받은 친구 요청을 수락합니다.")
    public ResponseEntity<ApiResponse<Void>> acceptFriendRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable int targetMemberId){
        friendCommandService.acceptFriendRequest(customUserDetails.getUserId(), targetMemberId);
        return ResponseEntity.ok(ApiResponse.success(null, "친구 요청이 수락되었습니다."));
    }


    /* 친구 요청 거절 */
    @PutMapping("/{targetMemberId}/reject")
    @Operation(summary = "친구 요청 거절", description = "받은 친구 요청을 거절합니다.")
    public ResponseEntity<ApiResponse<Void>> rejectFriendRequest(@PathVariable int targetMemberId){
        return ResponseEntity.ok(ApiResponse.success(null, "친구 요청이 거절되었습니다."));
    }

    /* 친구 삭제 */
    @DeleteMapping("/{targetMemberId}")
    @Operation(summary = "친구 삭제", description = "친구 관계를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteFriend(@PathVariable int targetMemberId){
        return ResponseEntity.ok(ApiResponse.success(null, "친구 삭제를 성공했습니다. "));
    }
}
