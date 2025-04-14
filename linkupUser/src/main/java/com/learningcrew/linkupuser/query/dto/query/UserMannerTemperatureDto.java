package com.learningcrew.linkupuser.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Schema(description = "사용자의 매너온도 정보")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMannerTemperatureDto {

    @Schema(description = "현재 매너온도", example = "37.5")
    private Double mannerTemperature;

}

