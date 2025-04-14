package com.learningcrew.linkup.place.query.mapper;

import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.query.dto.request.FavoriteRequest;
import com.learningcrew.linkup.place.query.dto.request.PlaceListRequest;
import com.learningcrew.linkup.place.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceMapper {

    // 회원의 즐겨찾기 목록 조회 (페이징 적용)
    List<PlaceDto> selectAllPlaces(PlaceListRequest placeListRequest);

    List<PlaceDto> selectAllPlacesByAdmin(PlaceListRequest placeListRequest);

    PlaceDetailResponse selectPlaceDetail(@Param("placeId")int placeId);

    List<PlaceDto> selectPlacesByOwner(PlaceListRequest placeListRequest);

    long countPlaces(PlaceListRequest placeListRequest);

    long countPlacesByAdmin(PlaceListRequest placeListRequest);

    long countPlacesByOwner(PlaceListRequest placeListRequest);

    Place selectPlaceById(@Param("placeId") int placeId);


    Integer findOwnerIdByPlaceId(@Param("placeId") int placeId);

    String findPlaceNameByPlaceId(@Param("placeId") int placeId);

    PlaceDetailResponse selectBasicPlaceDetail(int placeId);

    // 장소 세부 정보 조회
    List<String> selectImages(int placeId);

    List<OperationTimeResponse> selectOperationTimes(int placeId);

    List<PlaceReviewResponse> selectReviews(int placeId);

}
