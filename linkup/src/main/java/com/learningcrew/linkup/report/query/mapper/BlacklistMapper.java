package com.learningcrew.linkup.report.query.mapper;

import com.learningcrew.linkup.report.query.dto.response.BlacklistDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlacklistMapper {

    // 블랙리스트 목록 조회 (블랙리스트 ID 및 사용자 ID 필터링 추가)
    List<BlacklistDTO> selectBlacklists(@Param("memberId") Long memberId,
                                        @Param("size") int size,
                                        @Param("offset") int offset);

    // 블랙리스트 목록 전체 건수 조회 (블랙리스트 ID 및 사용자 ID 필터링 추가)
    long countBlacklists(@Param("memberId") Long memberId);

    // 블랙리스트 상세 조회
    BlacklistDTO selectBlacklistById(@Param("memberId") Long memberId);
}
