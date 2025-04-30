package com.learningcrew.linkup.report.query.mapper;

import com.learningcrew.linkup.report.query.dto.request.ObjectionSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.ObjectionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ObjectionMapper {

    // 이의 제기 목록 조회 (상태, 사용자 ID, 패널티 유형 필터링)
    List<ObjectionDTO> selectObjections(@Param("statusId") Long statusId,
                                        @Param("memberId") Long memberId,
                                        @Param("penaltyTypeId") Long penaltyTypeId,
                                        @Param("size") int size,
                                        @Param("offset") int offset);

    // 이의 제기 목록 전체 건수 조회
    long countObjections(@Param("statusId") Long statusId,
                         @Param("memberId") Long memberId,
                         @Param("penaltyTypeId") Long penaltyTypeId);

    // 이의 제기 상세 조회
    ObjectionDTO selectObjectionById(Long objectionId);
}
