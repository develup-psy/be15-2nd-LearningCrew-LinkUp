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
public class ReportUserListResponse<T> {

    @Schema(
            description = "사용자별 신고 내역 목록 (신고자 또는 피신고자)"
    )
    private List<T> userList;

    @Schema(description = "페이지네이션 정보")
    private Pagination pagination;
}
