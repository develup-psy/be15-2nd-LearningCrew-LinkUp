package com.learningcrew.linkupuser.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "참여한 모임 이력 정보")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserMeetingHistoryDto {

    @Schema(description = "모임 ID", example = "301")
    private Long meetingId;

    @Schema(description = "모임 제목", example = "봄맞이 등산")
    private String title;

    @Schema(description = "모임 일시", example = "2025-03-30T10:00:00")
    private LocalDateTime date;

    @Schema(description = "모임 장소", example = "북한산 국립공원")
    private String place;

    @Schema(description = "참여 상태", example = "참여 완료")
    private String status;
}

