package com.learningcrew.linkup.point.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionRequest {
    private int userId;
    private int amount;
    private String transactionType;
}
