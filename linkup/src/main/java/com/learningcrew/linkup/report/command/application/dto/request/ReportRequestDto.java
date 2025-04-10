package com.learningcrew.linkup.report.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReportRequestDto {

    @NotNull(message = "피신고자 ID는 필수입니다.")
    private Long targetMemberId; // 피신고자 ID

    @NotNull(message = "신고 대상 ID는 필수입니다.")
    private Long targetId; // 신고 대상 ID (게시글, 댓글 등)

    @NotBlank(message = "신고 유형은 필수입니다.")
    private String reportType; // 신고 유형 (enum 사용: "post", "comment", "review")

    @NotBlank(message = "신고 내용은 필수입니다.")
    private String reportContent; // 신고 내용

    private List<Long> reasonIds; // 신고 사유 목록
}
