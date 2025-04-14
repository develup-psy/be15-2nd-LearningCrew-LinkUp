package com.learningcrew.linkup.community.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingParticipationCreateRequest {
    private int memberId;
}
