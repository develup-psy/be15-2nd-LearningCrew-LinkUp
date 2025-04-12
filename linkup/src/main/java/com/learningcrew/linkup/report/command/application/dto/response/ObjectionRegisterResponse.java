package com.learningcrew.linkup.report.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ObjectionRegisterResponse {

    private Long objectionId;
    private Long penaltyId;
    private Integer memberId;
    private Integer statusId;
    private String message;
}
