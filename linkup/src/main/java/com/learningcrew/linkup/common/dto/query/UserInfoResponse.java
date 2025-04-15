package com.learningcrew.linkup.common.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Integer userId;
    private String email;
    private String nickname;
    private String role;         // 예: USER, BUSINESS, ADMIN
    private String status;       // 예: PENDING, ACTIVE
    private String profileImageUrl;
}
