package com.learningcrew.linkup.linker.query.dto.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FriendRequestStatusDTO {

    @Schema(description = "요청/응답 대상자 ID", example = "7")
    private Integer targetMemberId;

    @Schema(description = "대상자 닉네임", example = "한화링커")
    private String nickname;

    @Schema(description = "요청 상태", example = "PENDING")
    private String status;

    @Schema(description = "요청 일시", example = "2025-04-12T11:03:00")
    private LocalDateTime requestedAt;

    @Schema(description = "응답 일시", example = "2025-04-12T11:10:00", nullable = true)
    private LocalDateTime respondedAt;
}
