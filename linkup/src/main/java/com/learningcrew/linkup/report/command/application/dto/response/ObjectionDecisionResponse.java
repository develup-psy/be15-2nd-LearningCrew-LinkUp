package com.learningcrew.linkup.report.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 이의 제기 처리 응답 DTO
 */
@Getter
@Builder
public class ObjectionDecisionResponse {

    private Long objectionId;
    private Integer statusId;
    private String statusName;
    private LocalDateTime processedAt;
    private String message;
}
