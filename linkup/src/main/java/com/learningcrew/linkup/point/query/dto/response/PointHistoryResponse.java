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
public class PointHistoryResponse {
    private int pointTransactionId;
    private int amount;
    private String transactionType; // CHARGE, PAYMENT, REFUND, WITHDRAW 등
    private LocalDateTime createdAt;
}