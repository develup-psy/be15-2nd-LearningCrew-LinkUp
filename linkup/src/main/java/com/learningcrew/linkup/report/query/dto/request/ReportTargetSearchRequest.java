package com.learningcrew.linkup.report.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportTargetSearchRequest {

    @Schema(description = "페이지 번호", example = "1", defaultValue = "1")
    private int page = 1;

    @Schema(description = "페이지당 항목 수", example = "10", defaultValue = "10")
    private int size = 10;

    @Schema(description = "피신고 대상 유형 (USER, POST, COMMENT)", example = "USER")
    private String targetType;

    @Schema(description = "피신고 대상 ID", example = "87")
    private Long targetId;

    @Schema(description = "활성 여부 (Y=활성, N=비활성)", example = "Y")
    private String isActive;

    public int getOffset() {
        return (page - 1) * size;
    }
}
