package com.learningcrew.linkup.security.command.application.controller;

import com.learningcrew.linkup.security.command.application.dto.request.LoginRequest;
import com.learningcrew.linkup.security.command.application.dto.request.RefreshTokenRequest;
import com.learningcrew.linkup.security.command.application.dto.response.TokenResponse;
import com.learningcrew.linkup.security.command.application.service.AuthService;
import com.learningcrew.linkup.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Authentication API", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /* 자체 로그인 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request){
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(token));
    }

    /* 토큰 재발급 */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> getAccessTokenByRefreshToken(@Valid @RequestBody RefreshTokenRequest request){
        TokenResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request){
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
