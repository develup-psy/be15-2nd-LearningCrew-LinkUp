package com.learningcrew.linkup.linker.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "친구 목록 조회 Response DTO ")
public class FriendInfoResponse {

    @Schema(description = "친구 회원 ID", example = "8")
    private Integer friendId;

    @Schema(description = "친구 닉네임", example = "링커짱")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://cdn.linker.com/profile/8.jpg")
    private String profileImageUrl;

    @Schema(description = "친구 수락 시점", example = "2025-04-10T21:30:00")
    private LocalDateTime connectedAt;
}

