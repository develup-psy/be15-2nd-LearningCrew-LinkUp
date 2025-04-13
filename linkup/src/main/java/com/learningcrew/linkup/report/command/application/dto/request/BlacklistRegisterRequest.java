package com.learningcrew.linkup.report.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// 블랙리스트 등록 요청 DTO
@Getter
@Setter
public class BlacklistRegisterRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Integer memberId;

    @NotBlank(message = "블랙리스트 등록 사유는 필수입니다.")
    private String reason;
}
