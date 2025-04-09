package com.learningcrew.linkup.meeting.query.dto.response;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class MemberProfileDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int member_id;

    private String nickname;
    
    // 성별 등 정보 더 추가
}
