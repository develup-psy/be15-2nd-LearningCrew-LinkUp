package com.learningcrew.linkup.meeting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interested_meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BestPlayer {

    @EmbeddedId
    private BestPlayerId bestPlayerId;

    @ManyToOne
    @MapsId("meetingId") // EmbeddedId의 필드명
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @MapsId("memberId") // EmbeddedId의 필드명
    @JoinColumn(name = "member_id")
    private Member member;

}

