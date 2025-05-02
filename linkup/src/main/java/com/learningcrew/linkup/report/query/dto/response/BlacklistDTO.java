package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistDTO {

    @Schema(description = "블랙리스트 대상 사용자 ID", example = "42")
    private Long memberId;

    @Schema(description = "사용자 이름", example = "사용자")
    private String userName;

    @Schema(description = "블랙리스트 사유", example = "지속적인 신고 누적")
    private String reason;

    @Schema(description = "블랙리스트 등록 일시", example = "2025-04-30T10:15:30")
    private LocalDateTime createdAt;
}
