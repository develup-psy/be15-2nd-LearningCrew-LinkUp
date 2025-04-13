package com.learningcrew.linkup.report.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentReportRequest {

    @NotNull(message = "신고자 ID는 필수입니다.")
    private Integer reporterMemberId;

    @NotNull(message = "신고 대상자 ID는 필수입니다.")
    private Integer targetMemberId;

    @NotNull(message = "댓글 ID는 필수입니다.")
    private Long commentId;

    @NotNull(message = "신고 유형 ID는 필수입니다.")
    private Byte reportTypeId;

    @NotNull(message = "신고 사유는 필수입니다.")
    @Size(min = 1, max = 1000)
    private String reason;
}
