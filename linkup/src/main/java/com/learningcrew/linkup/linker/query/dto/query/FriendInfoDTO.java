package com.learningcrew.linkup.linker.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FriendInfoDTO {

    @Schema(description = "친구 회원 ID", example = "8")
    private Integer friendId;

    @Schema(description = "친구 닉네임", example = "링커짱")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://cdn.linker.com/profile/8.jpg")
    private String profileImageUrl;

    @Schema(description = "친구 수락 시각", example = "2025-04-12T13:45:00")
    private LocalDateTime connectedAt;
}

