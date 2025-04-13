package com.learningcrew.linkup.linker.query.dto.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FriendRequestStatusDTO {

    @Schema(description = "대상자 ID", example = "7")
    private Integer targetMemberId;

    @Schema(description = "대상자 닉네임", example = "한화링커")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://cdn.linker.com/profile/8.jpg")
    private String profileImageUrl;
}
