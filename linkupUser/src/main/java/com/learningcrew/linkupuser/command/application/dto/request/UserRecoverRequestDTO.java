package com.learningcrew.linkupuser.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "회원 복구 요청 DTO")
public class UserRecoverRequestDTO {
    @Schema(description = "이메일",  requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "복구신청할 이메일을 필수적으로 입력해주세요")
    String email;
    @Schema(description = "비밀번호",  requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호를 필수적으로 입력해주세요")
    String password;
}
