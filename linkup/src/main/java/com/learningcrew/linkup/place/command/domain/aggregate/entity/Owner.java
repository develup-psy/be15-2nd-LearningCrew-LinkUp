package com.learningcrew.linkup.place.command.domain.aggregate.entity;

import com.learningcrew.linkup.common.domain.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "owner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Owner {

    @Id
    @Column(name = "owner_id")
    private int ownerId;  // ownerId가 user의 id와 동일함

    private int statusId;
    private String businessRegistrationDocumentUrl;
    private LocalDateTime authorizedAt;
    private String rejectionReason;

    public void approve(LocalDateTime authorizedAt, int statusId) {
        this.statusId = statusId;
        this.authorizedAt = authorizedAt;
        this.rejectionReason = null;
    }

    public void reject(String rejectionReason, int statusId) {
        this.statusId = statusId;
        this.rejectionReason = rejectionReason;
        this.authorizedAt = null;
    }
}
