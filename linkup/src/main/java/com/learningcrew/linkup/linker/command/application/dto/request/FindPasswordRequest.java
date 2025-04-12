package com.learningcrew.linkup.linker.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "비밀번호 찾기 요청 DTO")
public class FindPasswordRequest {
    @Email
    @NotBlank
    @Schema(description = "이메일", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
