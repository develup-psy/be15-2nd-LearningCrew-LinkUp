package com.learningcrew.linkup.report.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectionSearchRequest {

    @Schema(description = "페이지 번호", example = "1", defaultValue = "1")
    private int page = 1;

    @Schema(description = "페이지당 항목 수", example = "10", defaultValue = "10")
    private int size = 10;

    @Schema(description = "사용자 ID", example = "50")
    private Long memberId;

    @Schema(description = "이의제기 상태 ID (예: 1=처리중, 2=처리완료)", example = "1")
    private Long statusId;

    @Schema(description = "제재 유형 ID (예: 1=댓글, 2=후기)", example = "2")
    private Long penaltyTypeId;

    public int getOffset() {
        return (page - 1) * size;
    }
}
