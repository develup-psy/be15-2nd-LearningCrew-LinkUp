package com.learningcrew.linkup.meeting.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Update;
import org.hibernate.annotations.CreationTimestamp;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime participatedAt;
    private int statusId;

}