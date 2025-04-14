package com.learningcrew.linkupuser.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "프로필 수정 DTO")
public class ProfileUpdateRequest {
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "프로필 이미지")
    private String profileImageUrl;
    @Schema(description = "자기소개")
    private String introduction;
}
