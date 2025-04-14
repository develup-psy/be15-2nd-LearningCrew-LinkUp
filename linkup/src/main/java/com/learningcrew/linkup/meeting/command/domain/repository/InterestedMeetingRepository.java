package com.learningcrew.linkup.meeting.command.domain.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.InterestedMeeting;

public interface InterestedMeetingRepository {
    InterestedMeeting save(InterestedMeeting interestedMeeting);

    void delete(InterestedMeeting deleteInterestedMeeting);

//    boolean existsByMeetingIdAndMemberId(int meetingId, int memberId);

    boolean existsByInterestedMeetingId_MeetingIdAndInterestedMeetingId_MemberId(int meetingId, int memberId);
}
