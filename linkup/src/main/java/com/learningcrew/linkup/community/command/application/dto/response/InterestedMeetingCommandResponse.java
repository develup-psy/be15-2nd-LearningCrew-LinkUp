package com.learningcrew.linkup.community.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterestedMeetingCommandResponse {
    private int meetingId;
    private int memberId;
}
