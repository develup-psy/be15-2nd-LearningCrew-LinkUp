package com.learningcrew.linkup.point.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySettlementResponse {
    private int ownerId;
    private int totalAmount;
    private boolean alreadyRequested;
}
