package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserMeetingActivityResponse {

    // 이전에 참여했던 모임 목록
    private final List<MeetingDTO> pastMeetings;

    // 예정된 모임 목록 (오늘 이후 참여 예정)
    private final List<MeetingDTO> upcomingMeetings;
}
