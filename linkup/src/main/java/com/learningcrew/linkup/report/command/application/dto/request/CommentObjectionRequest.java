package com.learningcrew.linkup.report.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentObjectionRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Integer memberId;

    @NotBlank(message = "이의 제기 사유는 필수입니다.")
    private String reason;
}
