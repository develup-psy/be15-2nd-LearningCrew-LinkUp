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

    @Schema(description = "모임 제목", example = "러닝 모임")
    private String meetingTitle;

    @Schema(description = "개설자 닉네임", example = "친구홍")
    private String leaderNickName;

    @Schema(description = "모임 일시", example = "2025-04-01T19:00:00")
    private LocalDateTime meetingDate;

    @Schema(description = "모임 장소", example = "서울특별시 마포구 월드컵경기장 풋살장")
    private String meetingPlace;

    @Schema(description = "모임 장소", example = "한강 공원")
    private String customPlaceAddress;


    @Schema(description = "위도", example = "36.234234234")
    private double latitude;

    @Schema(description = "위도", example = "18.234234234")
    private double longitude;
}

