package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.report.query.dto.request.ObjectionSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.ObjectionDTO;
import com.learningcrew.linkup.report.query.dto.response.ObjectionListResponse;

import java.util.List;

public interface ObjectionQueryService {

    // 이의 제기 목록 조회
    ObjectionListResponse getObjections(ObjectionSearchRequest request);

    // 이의 제기 상세 조회
    ObjectionDTO getObjectionById(Long objectionId);
}
