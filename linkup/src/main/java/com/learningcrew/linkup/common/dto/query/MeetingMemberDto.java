package com.learningcrew.linkup.common.dto.query;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MeetingMemberDto {
    private int memberId;                      // 고유 ID
    private String gender;                    // 성별
    private String nickname;                  // 닉네임
    private LocalDate birthDate;              // 생년월일
    private String introduction;              // 자기소개
    private Double mannerTemperature;     // 매너 온도
    private String profileImageUrl;           // 프로필 이미지
}