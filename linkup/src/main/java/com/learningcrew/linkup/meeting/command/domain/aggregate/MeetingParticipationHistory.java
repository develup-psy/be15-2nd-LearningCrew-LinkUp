package com.learningcrew.linkup.meeting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "meeting_participation_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MeetingParticipationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long participationId;
    private int memberId;
    private int meetingId;
    private LocalDateTime participatedAt;
    // statusId 필요

}