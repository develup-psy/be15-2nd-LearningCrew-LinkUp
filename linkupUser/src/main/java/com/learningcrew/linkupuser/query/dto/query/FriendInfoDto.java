package com.learningcrew.linkupuser.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendInfoDto {

    @Schema(description = "친구 회원 ID", example = "8")
    private Integer friendId;

    @Schema(description = "친구 닉네임", example = "링커짱")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://cdn.linker.com/profile/8.jpg")
    private String profileImageUrl;
}

