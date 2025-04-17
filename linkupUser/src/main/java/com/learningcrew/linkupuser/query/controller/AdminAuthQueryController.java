package com.learningcrew.linkupuser.query.controller;


import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.response.UserListResponse;
import com.learningcrew.linkupuser.query.service.UserQueryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name="관리자 회원 조회")
public class AdminAuthQueryController {
    private final UserQueryServiceImpl userQueryService;

    /* 전체 회원 조회 */
    @GetMapping("/users")
    @Operation(description = "전체 회원 조회")
    public ResponseEntity<ApiResponse<UserListResponse>> getUserList() {
        UserListResponse response = userQueryService.getUserList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
