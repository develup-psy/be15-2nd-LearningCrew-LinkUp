package com.learningcrew.linkup.linker.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResetPasswordRequest {

    @Email
    @NotBlank
    @Schema(description = "이메일", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @NotBlank
    @Schema(description = "비밀번호 재설정 토큰", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank
    @NotBlank
    @Schema(description = "새로운 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
