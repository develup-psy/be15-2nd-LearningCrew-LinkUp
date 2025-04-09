package com.learningcrew.linkup.meeting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interested_meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class InterestedMeeting {

    @EmbeddedId
    private InterestedMeetingId interestedMeetingId;

    @ManyToOne
    @MapsId("meetingId") // EmbeddedId의 필드명
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @MapsId("memberId") // EmbeddedId의 필드명
    @JoinColumn(name = "member_id")
    private Member member;

}
