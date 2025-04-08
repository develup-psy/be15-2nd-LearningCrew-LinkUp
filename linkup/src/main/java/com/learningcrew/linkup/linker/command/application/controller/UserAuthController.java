package com.learningcrew.linkup.linker.command.application.controller;

import com.learningcrew.linkup.linker.command.application.dto.UserCreateRequest;
import com.learningcrew.linkup.linker.command.application.service.UserCommandService;
import com.learningcrew.linkup.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "User API", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserAuthController {
    private final UserCommandService userCommandService;

    /* 회원 가입 */
    @PostMapping("/register")
    @Operation(
            summary = "회원가입", description = "이메일과 비밀번호, 전화번호 등의 정보를 입력하여 회원으로 가입할 수 있다."
    )
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserCreateRequest request) {
        userCommandService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    /* 이메일 찾기 */

    /* 비밀번호 찾기 - 비밀번호 재설정 url 발송 */

    /* 비밀번호 찾기 - 비밀번호 재설정 */

    /* 로그아웃 */




}
