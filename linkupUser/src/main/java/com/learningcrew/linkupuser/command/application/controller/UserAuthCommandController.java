package com.learningcrew.linkupuser.command.application.controller;


import com.learningcrew.linkupuser.command.application.dto.request.FindPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.request.LoginRequest;
import com.learningcrew.linkupuser.command.application.dto.request.RefreshTokenRequest;
import com.learningcrew.linkupuser.command.application.dto.request.ResetPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.response.TokenResponse;
import com.learningcrew.linkupuser.command.application.service.AuthCommandService;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthCommandController {
    private final AuthCommandService userAuthCommandService;

    /* 자체 로그인 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request){
        log.info("로그인 요청: email={}", request.getEmail());
        TokenResponse token = userAuthCommandService.login(request);
        log.info("로그인 성공: accessToken 발급 완료");
        return ResponseEntity.ok(ApiResponse.success(token));
    }

    /* 이메일 인증 */
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam("token") String token) {
        log.info("이메일 인증 요청: token={}", token);
        userAuthCommandService.verifyEmail(token);
        log.info("이메일 인증 성공");
        // 리다이렉트 주소를 헤더로 반환
        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .header("Location", "http://localhost:8080/api/v1/register/success")
                .build();
    }

    /* 토큰 재발급 */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> getAccessTokenByRefreshToken(@Valid @RequestBody RefreshTokenRequest request){
        log.info("토큰 재발급 요청: refreshToken={}", request.getRefreshToken());
        TokenResponse response = userAuthCommandService.refreshToken(request.getRefreshToken());
        log.info("토큰 재발급 성공: accessToken 및 refreshToken 갱신 완료");
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request){
        log.info("로그아웃 요청: refreshToken={}", request.getRefreshToken());
        userAuthCommandService.logout(request);
        log.info("로그아웃 처리 완료: refreshToken 삭제됨");
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 비밀번호 찾기 - 비밀번호 재설정 url 발송 */
    @PostMapping("/password/reset-link")
    @Operation(summary = "비밀번호 재설정 URL 발송", description = "이메일 조회 후 재설정 url을 이메일로 발송")
    public ResponseEntity<ApiResponse<Void>> sendPasswordResetUrl(@Valid @RequestBody FindPasswordRequest request) {
        userAuthCommandService.sendPasswordResetLink(request);
        return ResponseEntity.ok(ApiResponse.success(null, "비밀번호 재설정 메일을 전송했습니다."));
    }

    /* 비밀번호 재설정 */
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("비밀번호 재설정 요청: email={}, token={}, password={}", request.getEmail(), request.getToken(), request.getNewPassword());
        userAuthCommandService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "비밀번호가 변경되었습니다."));
    }

}
