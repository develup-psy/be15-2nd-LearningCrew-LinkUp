package com.learningcrew.linkup.meeting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "participant_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ParticipantReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    private int reviewerId;
    private int revieweeId;
    private int meetingId;
    private int score;
    private LocalDateTime createdAt;

}