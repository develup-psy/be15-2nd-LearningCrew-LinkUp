package com.learningcrew.linkup.report.query.dto.response;

import com.learningcrew.linkup.common.dto.Pagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTargetListResponse {

    @Schema(description = "피신고 대상 목록")
    private List<ReportTargetDTO> targetList;

    @Schema(description = "페이지네이션 정보")
    private Pagination pagination;
}
