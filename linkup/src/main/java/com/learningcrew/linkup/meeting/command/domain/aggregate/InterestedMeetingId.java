package com.learningcrew.linkup.meeting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class InterestedMeetingId implements Serializable {
    @Column(name = "meeting_id")
    private int meetingId;

    @Column(name = "member_id")
    private int memberId;
}
