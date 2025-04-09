package com.learningcrew.linkup.meeting.command.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MeetingDeleteRequest {
    @Min(value = 1)
    private int meetingId;
}
