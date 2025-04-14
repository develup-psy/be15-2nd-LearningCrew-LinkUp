package com.learningcrew.linkup.place.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.query.dto.request.PlaceListRequest;
import com.learningcrew.linkup.place.query.dto.response.*;
import com.learningcrew.linkup.place.query.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {
    private final PlaceMapper placeMapper;

    @Transactional(readOnly = true)
    public PlaceListResponse getPlaces(PlaceListRequest placeListRequest) {

        List<PlaceDto> places = placeMapper.selectAllPlaces(placeListRequest);
        long totalItems = placeMapper.countPlaces(placeListRequest);
        int page = placeListRequest.getPage();
        int size = placeListRequest.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return PlaceListResponse.builder()
                .place(places)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public PlaceListResponse getPlacesByAdmin(PlaceListRequest placeListRequest) {

        List<PlaceDto> places = placeMapper.selectAllPlacesByAdmin(placeListRequest);
        long totalItems = placeMapper.countPlacesByAdmin(placeListRequest);
        int page = placeListRequest.getPage();
        int size = placeListRequest.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return PlaceListResponse.builder()
                .place(places)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public PlaceDetailResponse getPlaceDetails(int placeId) {
        PlaceDetailResponse detail = placeMapper.selectBasicPlaceDetail(placeId); // 단건 기본 정보만

        detail.setImageUrl(placeMapper.selectImages(placeId));
        detail.setOperationTimes(placeMapper.selectOperationTimes(placeId));
        detail.setPlaceReviews(placeMapper.selectReviews(placeId));

        return detail;
    }


    @Transactional(readOnly = true)
    public PlaceListResponse getPlacesByOwner(PlaceListRequest request) {
        List<PlaceDto> places = placeMapper.selectPlacesByOwner(request);
        long totalItems = placeMapper.countPlacesByOwner(request);
        int page = request.getPage();
        int size = request.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return PlaceListResponse.builder()
                .place(places)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }
    public Place getPlaceById(int placeId) {
        return placeMapper.selectPlaceById(placeId);
    }

}
