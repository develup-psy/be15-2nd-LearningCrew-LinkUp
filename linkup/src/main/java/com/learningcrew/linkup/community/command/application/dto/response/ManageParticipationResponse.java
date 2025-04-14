package com.learningcrew.linkup.community.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManageParticipationResponse {
    private long participationId;
    private String statusType;
}
