package com.learningcrew.linkup.community.command.domain.aggregate;

import com.learningcrew.linkup.meeting.command.domain.aggregate.BestPlayerId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interested_meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BestPlayer {

    @EmbeddedId
    private com.learningcrew.linkup.meeting.command.domain.aggregate.BestPlayerId bestPlayerId;

    public BestPlayer(BestPlayerId id) {
        this.bestPlayerId = id;
    }

    // 편의 메서드 추가 (필요할 경우)
    public int getMeetingId() {
        return bestPlayerId.getMeetingId();
    }

    public int getMemberId() {
        return bestPlayerId.getMemberId();
    }

}

