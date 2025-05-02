package com.learningcrew.linkup.report.query.dto.response;

import com.learningcrew.linkup.common.dto.Pagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PenaltyListResponse {

    @Schema(description = "제재 내역 목록")
    private List<PenaltyDTO> penalties;

    @Schema(description = "페이지네이션 정보")
    private Pagination pagination;
}
