package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.query.dto.request.BlacklistSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.BlacklistDTO;
import com.learningcrew.linkup.report.query.dto.response.BlacklistListResponse;
import com.learningcrew.linkup.report.query.mapper.BlacklistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlacklistQueryServiceImpl implements BlacklistQueryService {

    private final BlacklistMapper blacklistMapper;

    @Override
    public BlacklistListResponse getBlacklists(BlacklistSearchRequest request) {
        // 블랙리스트 목록 조회
        List<BlacklistDTO> blacklists = blacklistMapper.selectBlacklists(
                request.getMemberId(),
                request.getSize(),
                request.getOffset()
        );

        // 전체 블랙리스트 개수 조회
        long totalItems = blacklistMapper.countBlacklists(
                request.getMemberId()
        );

        // 페이징 정보 계산
        int totalPages = (int) Math.ceil((double) totalItems / request.getSize());

        // Pagination에 long 타입의 totalItems를 그대로 전달
        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalPage(totalPages)
                .totalItems(totalItems)  // totalItems을 long 그대로 전달
                .build();

        return BlacklistListResponse.builder()
                .blacklists(blacklists)
                .pagination(pagination) // Pagination 객체 전달
                .build();
    }

    @Override
    public BlacklistDTO getBlacklistById(Long memberId) {
        BlacklistDTO blacklist = blacklistMapper.selectBlacklistById(memberId);
        if (blacklist == null) {
            throw new BusinessException(ErrorCode.BLACKLIST_NOT_FOUND);
        }
        return blacklist;
    }

}
