package com.learningcrew.linkup.report.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

// 블랙리스트 등록 응답 DTO
@Getter
@Builder
public class BlacklistRegisterResponse {

    private Integer memberId;
    private String message;
}
