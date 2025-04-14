package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.UserMeetingActivityResponse;

public interface MeetingManageService {

    MeetingListResponse getAllMeetings(MeetingSearchRequest request);

    UserMeetingActivityResponse getUserMeetingActivity(int userId);
}
