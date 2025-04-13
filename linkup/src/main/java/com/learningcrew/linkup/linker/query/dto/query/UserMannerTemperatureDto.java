package com.learningcrew.linkup.linker.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "사용자의 매너온도 정보")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMannerTemperatureDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "현재 매너온도", example = "37.5")
    private Double temperature;

    @Schema(description = "매너 레벨", example = "좋음") // 예: 좋음, 보통, 주의
    private String level;

    @Schema(description = "마지막 업데이트 일시", example = "2025-04-10T18:20:00")
    private LocalDateTime lastUpdated;
}

