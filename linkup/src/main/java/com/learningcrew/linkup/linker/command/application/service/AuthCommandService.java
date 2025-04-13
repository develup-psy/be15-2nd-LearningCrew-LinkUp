package com.learningcrew.linkup.linker.command.application.service;

import com.learningcrew.linkup.linker.command.application.dto.request.FindPasswordRequest;
import com.learningcrew.linkup.linker.command.application.dto.request.LoginRequest;
import com.learningcrew.linkup.linker.command.application.dto.request.RefreshTokenRequest;
import com.learningcrew.linkup.linker.command.application.dto.request.ResetPasswordRequest;
import com.learningcrew.linkup.linker.command.application.dto.response.TokenResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthCommandService {

    TokenResponse login(LoginRequest request);

    TokenResponse refreshToken(String providedRefreshToken);

    void logout(@Valid RefreshTokenRequest request);

    void sendPasswordResetLink(@Valid FindPasswordRequest request);

    void resetPassword(@Valid ResetPasswordRequest request);

    void verifyEmail(String token);
}
