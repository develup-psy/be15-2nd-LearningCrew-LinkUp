package com.learningcrew.linkup.linker.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendResponse {
    @Schema(description = "요청자 ID", example = "5")
    @NotNull
    private Integer requesterId;

    @Schema(description = "응답 상태 (ACCEPTED 또는 REJECTED)", example = "ACCEPTED")
    @NotBlank
    private String responseStatus;
}
