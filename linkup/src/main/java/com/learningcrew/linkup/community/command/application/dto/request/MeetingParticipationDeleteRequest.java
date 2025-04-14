package com.learningcrew.linkup.community.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class MeetingParticipationDeleteRequest {
    private int memberId;
    private int meetingId;
//    private int statusId = 4;

}
