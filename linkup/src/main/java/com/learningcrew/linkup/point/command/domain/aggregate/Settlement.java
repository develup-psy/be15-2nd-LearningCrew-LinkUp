package com.learningcrew.linkup.point.command.domain.aggregate;

import com.learningcrew.linkup.place.command.domain.aggregate.entity.Owner;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "settlement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int settlementId;

    private int ownerId;

    private int statusId;

    private int amount;

    @CreatedDate
    private LocalDateTime requestedAt;

    private LocalDateTime completedAt;

    private String rejectionReason;

    // 정산 완료 처리
    public void complete(LocalDateTime completedAt, int completedStatusId) {
        this.completedAt = completedAt;
        this.statusId = completedStatusId;
    }

    // 정산 거절 처리
    public void reject(String reason, int rejectedStatusId) {
        this.rejectionReason = reason;
        this.statusId = rejectedStatusId;
    }
}