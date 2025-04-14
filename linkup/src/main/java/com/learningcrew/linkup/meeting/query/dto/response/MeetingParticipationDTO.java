package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingParticipationDTO {

    private long participationId;
    private int statusId;
    private int memberId;
    private int meetingId;
    private LocalDateTime participatedAt;
}
