package com.learningcrew.linkup.common.infrastructure;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.dto.query.MeetingMemberDto;
import com.learningcrew.linkup.common.dto.query.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/feign-test")
@RequiredArgsConstructor
public class FeignCommunicationTestController {

    private final UserFeignClient userFeignClient;
    private final MemberQueryClient memberQueryClient;

    // ===== UserFeignClient =====

    @PostMapping("/user/{userId}/point/increase")
    public ResponseEntity<ApiResponse<Void>> testIncreasePoint(@PathVariable int userId, @RequestParam int amount) {
        userFeignClient.increasePoint(userId, amount);
        return ResponseEntity.ok(ApiResponse.success(null,"포인트 증가 성공"));
    }

    @PostMapping("/user/{userId}/point/decrease")
    public ResponseEntity<ApiResponse<Void>> testDecreasePoint(@PathVariable int userId, @RequestParam int amount) {
        userFeignClient.decreasePoint(userId, amount);
        return ResponseEntity.ok(ApiResponse.success(null,"포인트 감소 성공"));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<UserInfoResponse> testGetUserById(@PathVariable int userId) {
        return ApiResponse.success(userFeignClient.getUserById(userId));
    }

    @GetMapping("/user/{userId}/email")
    public ApiResponse<String> testGetEmail(@PathVariable int userId) {
        log.info("userId = {}", userId); // ← 반드시 로그 찍어보기
        return ApiResponse.success(userFeignClient.getEmailByUserId(userId));
    }

    @GetMapping("/user/{userId}/exists")
    public ApiResponse<Boolean> testUserExists(@PathVariable int userId) {
        return ApiResponse.success(userFeignClient.existsUser(userId));
    }

    @GetMapping("/user/{userId}/username")
    public ApiResponse<String> testGetUserName(@PathVariable int userId) {
        return ApiResponse.success(userFeignClient.getUserNameByUserId(userId));
    }

    @GetMapping("/user/{userId}/point")
    public ApiResponse<Integer> testGetPoint(@PathVariable int userId) {
        return ApiResponse.success(userFeignClient.getPointBalance(userId));
    }

    // ===== MemberQueryClient =====

    @GetMapping("/member/{memberId}/meeting-info")
    public ApiResponse<MeetingMemberDto> testGetMeetingMember(@PathVariable int memberId) {
        return memberQueryClient.getMemberById(memberId);
    }
}
