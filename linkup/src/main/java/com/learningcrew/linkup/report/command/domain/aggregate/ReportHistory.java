package com.learningcrew.linkup.report.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 신고 이력
@Entity
@Table(name = "report_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer id;

    @Column(name = "reporter_member_id", nullable = false)
    private Integer reporterId;

    @Column(name = "target_member_id", nullable = false)
    private Integer targetId;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "report_type_id", nullable = false)
    private Byte reportTypeId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
