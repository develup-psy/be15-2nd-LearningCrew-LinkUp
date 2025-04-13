package com.learningcrew.linkup.meeting.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingParticipationCreateRequest {
    private int memberId;
}
