package com.learningcrew.linkup.point.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementDetailResponse {
    private int settlementId;
    private int ownerId;
    private int amount;
    private int statusId;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private String rejectionReason;
}
