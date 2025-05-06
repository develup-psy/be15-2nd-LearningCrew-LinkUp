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
public class BlacklistListResponse {

    @Schema(description = "블랙리스트 사용자 목록")
    private List<BlacklistDTO> blacklists;

    @Schema(description = "페이지네이션 정보")
    private Pagination pagination;
}
