package com.learningcrew.linkup.place.query.dto.response;
import com.learningcrew.linkup.common.dto.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AdminPlaceListResponse {
    private List<AdminPlaceDto> adminPlaces;
    private final Pagination pagination;
}

