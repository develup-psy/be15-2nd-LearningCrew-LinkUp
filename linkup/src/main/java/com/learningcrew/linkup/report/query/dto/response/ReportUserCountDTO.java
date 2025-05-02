package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportUserCountDTO {

    @Schema(description = "사용자 ID", example = "42")
    private Long memberId;

    @Schema(description = "사용자 이름", example = "사용자")
    private String memberName;

    @Schema(description = "신고 횟수", example = "5")
    private Long reportCount;

    @Schema(description = "최근 신고 일시", example = "2025-04-30T12:00:00")
    private LocalDateTime lastReportDate;
}
