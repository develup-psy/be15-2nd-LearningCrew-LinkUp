package com.learningcrew.linkup.point.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointBalanceResponse {
    private int balance;
    private List<PointHistoryResponse> historyList;
}