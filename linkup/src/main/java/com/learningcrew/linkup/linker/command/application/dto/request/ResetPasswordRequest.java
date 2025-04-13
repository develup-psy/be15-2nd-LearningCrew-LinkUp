package com.learningcrew.linkup.linker.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Schema(description = "비밀번호 재설정 토큰", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
    @Schema(description = "새로운 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
