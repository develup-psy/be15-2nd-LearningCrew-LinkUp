package com.learningcrew.linkup.report.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlacklistSearchRequest {

    @Schema(description = "페이지 번호", example = "1", defaultValue = "1")
    private int page = 1;

    @Schema(description = "페이지당 항목 수", example = "10", defaultValue = "10")
    private int size = 10;

    @Schema(description = "블랙리스트 ID", example = "101")
    private Long blacklistId;

    @Schema(description = "사용자 ID (블랙리스트 대상)", example = "50")
    private Long memberId;

    public int getOffset() {
        return (page - 1) * size;
    }
}
