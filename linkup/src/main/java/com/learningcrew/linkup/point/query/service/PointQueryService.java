package com.learningcrew.linkup.point.query.service;

import com.learningcrew.linkup.point.query.dto.response.AccountResponse;
import com.learningcrew.linkup.point.query.dto.response.MonthlySettlementResponse;
import com.learningcrew.linkup.point.query.dto.response.PointHistoryResponse;
import com.learningcrew.linkup.point.query.dto.response.SettlementDetailResponse;

import java.util.List;

public interface PointQueryService {
    AccountResponse getAccount(int userId);

    List<PointHistoryResponse> getPointHistory(int i);

    List<SettlementDetailResponse> getAllSettlements(int i);

    MonthlySettlementResponse getMonthlySettlement(int i, int year, int month);
}