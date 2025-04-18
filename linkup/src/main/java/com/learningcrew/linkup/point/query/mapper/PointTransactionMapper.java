package com.learningcrew.linkup.point.query.mapper;

import com.learningcrew.linkup.point.query.dto.response.MonthlySettlementResponse;
import com.learningcrew.linkup.point.query.dto.response.PointHistoryResponse;
import com.learningcrew.linkup.point.query.dto.response.SettlementDetailResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PointTransactionMapper {
    List<PointHistoryResponse> findPointHistoriesByUserId(int userId);

    MonthlySettlementResponse findMonthlySettlementAmount(int ownerId, int year, int month);

    List<SettlementDetailResponse> findAllByOwnerId(int userId);
}
