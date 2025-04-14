package com.learningcrew.linkup.meeting.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingBalanceCheckResponse {
    private boolean check;
    private String message;
}
