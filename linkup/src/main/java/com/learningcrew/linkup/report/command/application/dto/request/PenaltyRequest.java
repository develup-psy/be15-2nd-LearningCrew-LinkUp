package com.learningcrew.linkup.report.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PenaltyRequest {

    @NotBlank(message = "제재 사유는 필수입니다.")
    private String reason;
}
