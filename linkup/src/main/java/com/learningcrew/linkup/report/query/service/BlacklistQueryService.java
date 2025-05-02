package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.report.query.dto.request.BlacklistSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.BlacklistDTO;
import com.learningcrew.linkup.report.query.dto.response.BlacklistListResponse;

public interface BlacklistQueryService {

    /**
     * 블랙리스트 목록을 조회합니다. 필터링과 페이징을 처리합니다.
     *
     * @param request BlacklistSearchRequest - 블랙리스트 조회 요청
     * @return BlacklistListResponse - 블랙리스트 목록 응답
     */
    BlacklistListResponse getBlacklists(BlacklistSearchRequest request);

    /**
     * 블랙리스트 상세 정보를 조회합니다.
     *
     * @param memberId 블랙리스트 ID
     * @return BlacklistDTO - 블랙리스트 상세 정보
     */
    BlacklistDTO getBlacklistById(Long memberId);
}
