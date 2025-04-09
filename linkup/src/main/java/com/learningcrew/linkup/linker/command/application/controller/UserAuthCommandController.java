package com.learningcrew.linkup.linker.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.linker.command.application.dto.LoginRequest;
import com.learningcrew.linkup.linker.command.application.dto.RefreshTokenRequest;
import com.learningcrew.linkup.linker.command.application.dto.TokenResponse;
import com.learningcrew.linkup.linker.command.application.service.UserAuthCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthCommandController {
    private final UserAuthCommandService userAuthCommandService;

    /* 자체 로그인 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request){
        TokenResponse token = userAuthCommandService.login(request);
        return ResponseEntity.ok(ApiResponse.success(token));
    }

    /* 토큰 재발급 */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> getAccessTokenByRefreshToken(@Valid @RequestBody RefreshTokenRequest request){
        TokenResponse response = userAuthCommandService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request){
        userAuthCommandService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
