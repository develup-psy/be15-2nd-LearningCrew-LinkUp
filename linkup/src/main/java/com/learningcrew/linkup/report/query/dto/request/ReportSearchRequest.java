package com.learningcrew.linkup.report.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSearchRequest {

    @Schema(description = "페이지 번호", example = "1", defaultValue = "1")
    private int page = 1;

    @Schema(description = "페이지당 항목 수", example = "10", defaultValue = "10")
    private int size = 10;

    @Schema(description = "신고자 ID", example = "32")
    private Long reporterMemberId;

    @Schema(description = "피신고자 ID", example = "45")
    private Long targetMemberId;

    @Schema(description = "신고 유형 ID", example = "2")
    private Long reportTypeId;

    @Schema(description = "신고 상태 ID", example = "1") // 예: 1=처리중, 2=처리완료 등
    private Long statusId;

    public int getOffset() {
        return (page - 1) * size;
    }
}
