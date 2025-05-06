package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.report.query.dto.request.PenaltySearchRequest;
import com.learningcrew.linkup.report.query.dto.response.PenaltyDTO;
import com.learningcrew.linkup.report.query.dto.response.PenaltyListResponse;

import java.util.List;

public interface PenaltyQueryService {

    // 제재 내역 조회 (전체, 유형별, 사용자별 통합, 페이징 및 필터링 처리)
    PenaltyListResponse getPenalties(PenaltySearchRequest request);

    // 제재 상세 조회
    PenaltyDTO getPenaltyById(Long penaltyId);

}
