package com.learningcrew.linkupuser.command.domain.aggregate;

import com.learningcrew.linkupuser.common.domain.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Builder
public class Friend {

    @EmbeddedId
    private FriendId id;

    @ManyToOne
    @JoinColumn(name="status_id", nullable = false)
    private Status status;

    @CreatedDate
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;

    public void updateStatus(Status acceptedStatus) {
        this.status = acceptedStatus;
    }

    public void updateResponededAt(LocalDateTime now) {
        this.respondedAt = now;
    }
}
