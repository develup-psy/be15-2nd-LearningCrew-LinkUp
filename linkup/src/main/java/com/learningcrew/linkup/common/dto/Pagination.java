package com.learningcrew.linkup.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Pagination {
    private int currentPage;
    private int totalPage;
    private long totalList; // 기존 totalItems 대신 totalList로 변경
}