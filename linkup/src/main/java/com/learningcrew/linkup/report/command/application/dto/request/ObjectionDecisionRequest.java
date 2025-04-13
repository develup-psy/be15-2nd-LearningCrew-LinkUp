package com.learningcrew.linkup.report.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 이의 제기 처리 요청 DTO
 */
@Getter
@Setter
public class ObjectionDecisionRequest {

    @NotNull(message = "이의 제기 ID는 필수입니다.")
    private Long objectionId;

    @NotNull(message = "처리 상태 ID는 필수입니다.")  // 2: 승인, 3: 거절
    private Integer statusId;
}
