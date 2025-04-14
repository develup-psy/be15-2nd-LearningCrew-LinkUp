package com.learningcrew.linkupuser.query.controller;


import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.response.UserListResponse;
import com.learningcrew.linkupuser.query.service.UserQueryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminAuthQueryController {
    private final UserQueryServiceImpl userQueryService;

    /* 전체 회원 조회 */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<UserListResponse>> getUserList() {
        UserListResponse response = userQueryService.getUserList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
