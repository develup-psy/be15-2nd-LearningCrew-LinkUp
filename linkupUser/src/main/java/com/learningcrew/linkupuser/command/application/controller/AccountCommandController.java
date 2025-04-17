package com.learningcrew.linkupuser.command.application.controller;

import com.learningcrew.linkupuser.command.application.service.AccountCommandService;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AccountCommandController {
    private final AccountCommandService accountCommandService;

    /* 포인트 증가 */
    @PostMapping("/{userId}/point/increase")
    public ResponseEntity<ApiResponse<Void>> increasePoint(@PathVariable int userId, @RequestParam int amount) {
        accountCommandService.increaseUserPoint(userId, amount);
        return ResponseEntity.ok(ApiResponse.success(null, "포인트 증가에 성공했습니다"));
    }

    /* 포인트 감소 */
    @PostMapping("/{userId}/point/decrease")
    public ResponseEntity<ApiResponse<Void>> decreasePoint(@PathVariable int userId, @RequestParam int amount) {
        accountCommandService.decreaseUserPoint(userId, amount);
        return ResponseEntity.ok(ApiResponse.success(null, "포인트 감소에 성공했습니다"));
    }
}
