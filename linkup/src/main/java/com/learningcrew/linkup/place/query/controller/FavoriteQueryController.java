package com.learningcrew.linkup.place.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.query.dto.request.FavoriteRequest;
import com.learningcrew.linkup.place.query.dto.response.FavoriteListResponse;
import com.learningcrew.linkup.place.query.service.FavoriteQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "장소 즐겨찾기 조회", description = "즐겨찾기된 장소 조회 API")
public class FavoriteQueryController {

    private final FavoriteQueryService favoriteQueryService;

    @GetMapping("/user/{memberId}/favorite")
    @Operation(summary = "즐겨찾기된 장소 조회", description = "즐겨찾기에 등록된 장소의 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<FavoriteListResponse>> getFavorite(
            @PathVariable("memberId") int memberId,
            FavoriteRequest favoriteRequest) {
        FavoriteListResponse response = favoriteQueryService.getFavoriteList(memberId, favoriteRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}