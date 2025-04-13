package com.learningcrew.linkup.linker.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "모임 정보")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMeetingDto {

    @Schema(description = "모임 ID", example = "301")
    private Long meetingId;

    @Schema(description = "모임 제목", example = "스터디 모임")
    private String title;

    @Schema(description = "개설자 닉네임", example = "친구홍")
    private String creatorNickname;

    @Schema(description = "모임 일시", example = "2025-04-01T19:00:00")
    private LocalDateTime date;

    @Schema(description = "모임 장소", example = "강남역 카페")
    private String place;
}

