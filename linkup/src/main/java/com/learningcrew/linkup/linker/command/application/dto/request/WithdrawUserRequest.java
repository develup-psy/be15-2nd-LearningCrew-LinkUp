package com.learningcrew.linkup.linker.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "회원 탈퇴 요청 DTO")
public class WithdrawUserRequest {
    @Schema(description = "비밀번호",  requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호를 필수적으로 입력해주세요")
    private String password;
}
