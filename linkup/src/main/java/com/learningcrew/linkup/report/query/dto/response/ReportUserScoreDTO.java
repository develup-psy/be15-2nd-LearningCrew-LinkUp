package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportUserScoreDTO {

    @Schema(description = "사용자 ID", example = "42")
    private Long memberId;

    @Schema(description = "사용자 이름", example = "사용자")
    private String memberName;

    @Schema(description = "신고 횟수", example = "7")
    private Integer reportCount;

    @Schema(description = "신고 점수", example = "140")
    private Integer reportScore;
}
