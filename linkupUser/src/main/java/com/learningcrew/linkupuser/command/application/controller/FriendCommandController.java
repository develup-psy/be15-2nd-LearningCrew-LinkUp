package com.learningcrew.linkupuser.command.application.controller;

import com.learningcrew.linkupuser.command.application.service.FriendCommandService;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
@Tag(name = "친구 관리", description = "친구 신청, 수락, 삭제")
public class FriendCommandController {

    private final FriendCommandService friendCommandService;

    /* 친구 요청 신청 */
    @PostMapping("/{targetMemberId}")
    @Operation(summary = "친구 요청 신청", description = "특정 회원에게 친구 요청을 보냅니다.")
    public ResponseEntity<ApiResponse<Void>> sendFriendRequest(@AuthenticationPrincipal String userId, @PathVariable int targetMemberId){
        log.info("친구 요청 신청 requestId: {}, addressId: {}", Integer.parseInt(userId), targetMemberId);
        friendCommandService.sendFriendRequest(Integer.parseInt(userId), targetMemberId);
        log.info("친구 요청 신청에 성공했습니다. ");
        return ResponseEntity.ok(ApiResponse.success(null, "친구 요청이 신청되었습니다."));
    }


    /* 친구 요청 수락 */
    @PutMapping("/{targetMemberId}/accept")
    @Operation(summary = "친구 요청 수락", description = "받은 친구 요청을 수락합니다.")
    public ResponseEntity<ApiResponse<Void>> acceptFriendRequest(@AuthenticationPrincipal String userId, @PathVariable int targetMemberId){
        friendCommandService.acceptFriendRequest(Integer.parseInt(userId), targetMemberId);
        return ResponseEntity.ok(ApiResponse.success(null, "친구 요청이 수락되었습니다."));
    }



    /* 친구 삭제 */
    @DeleteMapping("/{targetMemberId}")
    @Operation(summary = "친구 삭제", description = "친구 관계를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteFriend(@AuthenticationPrincipal String userId, @PathVariable int targetMemberId){
        friendCommandService.deleteFriend(Integer.parseInt(userId), targetMemberId);
        return ResponseEntity.ok(ApiResponse.success(null, "친구 삭제를 성공했습니다. "));
    }
}
