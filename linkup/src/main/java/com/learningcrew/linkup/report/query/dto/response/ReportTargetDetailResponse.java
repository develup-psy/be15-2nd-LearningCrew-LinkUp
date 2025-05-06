package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTargetDetailResponse {

    @Schema(description = "피신고 대상 유형 (USER, POST, COMMENT)", example = "USER")
    private String targetType;

    @Schema(description = "피신고 대상 ID", example = "123")
    private Long targetId;

    @Schema(description = "총 신고 횟수", example = "5")
    private Long reportCount;

    @Schema(description = "가장 최근 신고 일시", example = "2025-04-30T11:00:00")
    private LocalDateTime lastReportDate;

    @Schema(description = "활성 여부 (Y: 활성, N: 비활성)", example = "Y")
    private String isActive;

    @Schema(description = "피신고 대상에 대한 신고 목록")
    private List<ReportSimpleDTO> reportList;
}
