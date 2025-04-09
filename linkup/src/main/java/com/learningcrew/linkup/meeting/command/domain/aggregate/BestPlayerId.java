package com.learningcrew.linkup.meeting.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class BestPlayerId implements Serializable {
    @Column(name = "meeting_id")
    private int meetingId;

    @Column(name = "member_id")
    private int memberId;
}

