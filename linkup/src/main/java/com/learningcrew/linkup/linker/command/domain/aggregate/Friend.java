package com.learningcrew.linkup.linker.command.domain.aggregate;

import com.learningcrew.linkup.common.domain.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
@IdClass(FriendId.class)
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Builder
public class Friend {

    @Id
    private int requesterId;

    @Id
    private int addresseeId;

    @ManyToOne
    @JoinColumn(name="status_id", nullable = false)
    private Status status;

    @CreatedDate
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
