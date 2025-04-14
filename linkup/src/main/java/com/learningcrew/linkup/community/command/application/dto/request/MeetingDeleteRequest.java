package com.learningcrew.linkup.community.command.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingDeleteRequest {
    @Min(value = 1)
    private int meetingId;
}
