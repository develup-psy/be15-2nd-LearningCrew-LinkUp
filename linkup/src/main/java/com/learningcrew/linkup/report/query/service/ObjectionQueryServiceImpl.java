package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.query.dto.request.ObjectionSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.ObjectionDTO;
import com.learningcrew.linkup.report.query.dto.response.ObjectionListResponse;
import com.learningcrew.linkup.report.query.mapper.ObjectionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectionQueryServiceImpl implements ObjectionQueryService {

    private final ObjectionMapper objectionMapper;

    // 이의 제기 상세 조회
    @Override
    public ObjectionDTO getObjectionById(Long objectionId) {
        ObjectionDTO objection = objectionMapper.selectObjectionById(objectionId);
        if (objection == null) {
            throw new BusinessException(ErrorCode.OBJECTION_NOT_FOUND);
        }
        return objection;
    }


    @Override
    public ObjectionListResponse getObjections(ObjectionSearchRequest request) {
        // 이의 제기 목록 조회
        List<ObjectionDTO> objections = objectionMapper.selectObjections(
                request.getStatusId(),
                request.getMemberId(),
                request.getPenaltyTypeId(),
                request.getSize(),
                request.getOffset()
        );

        // 전체 이의 제기 개수 조회
        long totalItems = objectionMapper.countObjections(
                request.getStatusId(),
                request.getMemberId(),
                request.getPenaltyTypeId()
        );

        // 페이징 정보 계산
        int totalPages = (int) Math.ceil((double) totalItems / request.getSize());
        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalItems(totalItems)
                .totalPage(totalPages)
                .build();

        // 응답으로 ObjectionListResponse 반환
        return ObjectionListResponse.builder()
                .objections(objections)
                .pagination(pagination)
                .build();
    }
}
