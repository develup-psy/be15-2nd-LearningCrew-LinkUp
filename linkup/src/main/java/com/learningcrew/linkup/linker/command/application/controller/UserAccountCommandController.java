package com.learningcrew.linkup.linker.command.application.controller;

import com.learningcrew.linkup.linker.command.application.dto.LoginRequest;
import com.learningcrew.linkup.linker.command.application.dto.UserCreateRequest;
import com.learningcrew.linkup.linker.command.application.service.UserAuthCommandService;
import com.learningcrew.linkup.linker.command.application.service.UserAccountCommandService;
import com.learningcrew.linkup.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "User API", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserAccountCommandController {
    private final UserAccountCommandService userCommandService;
    private final UserAuthCommandService userAuthCommandService;

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


    /* 회원 탈퇴 */
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> deleteUser(){
        return null;
    }

    /* 계정 복구 */
    @PostMapping("/recover")
    public ResponseEntity<ApiResponse<Void>> recoverUser(){
        return null;
    }

    /* 비밀번호 찾기 */
    @PostMapping("/find-password")
    public ResponseEntity<ApiResponse<Void>> findPassword(@Valid @RequestBody LoginRequest request) {
        return null;
    }

    /* 비밀번호 재설정 */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody LoginRequest request) {
        return null;
    }


}
