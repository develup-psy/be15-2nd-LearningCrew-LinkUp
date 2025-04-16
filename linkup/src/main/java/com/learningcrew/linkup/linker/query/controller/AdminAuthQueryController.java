package com.learningcrew.linkup.linker.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.linker.query.dto.response.UserListResponse;
import com.learningcrew.linkup.linker.query.service.UserQueryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name="회원 관리",description = "회원 조회 API")
public class AdminAuthQueryController {
    private final UserQueryServiceImpl userQueryService;

    /* 전체 회원 조회 */
    @Operation(
            summary = "회원 조회",
            description = "관리자가 서비스에 가입된 모든 회원의 목록을 조회한다."
    )
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<UserListResponse>> getUserList() {
        UserListResponse response = userQueryService.getUserList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
