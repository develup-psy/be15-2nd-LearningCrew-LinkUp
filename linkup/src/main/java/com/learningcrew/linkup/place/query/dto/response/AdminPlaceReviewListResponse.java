package com.learningcrew.linkup.place.query.dto.response;

import com.learningcrew.linkup.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminPlaceReviewListResponse {
    private List<AdminPlaceReviewDto> reviews;
    private Pagination pagination;
}
