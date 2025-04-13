package com.learningcrew.linkup.linker.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "사용자 포인트 정보")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPointDto {

    @Schema(description = "사용자 ID", example = "1")
    private int userId;

    @Schema(description = "총 보유 포인트", example = "5000")
    private int totalPoints;
}

