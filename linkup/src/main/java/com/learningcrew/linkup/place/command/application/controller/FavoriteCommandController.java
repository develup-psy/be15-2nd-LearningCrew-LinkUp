package com.learningcrew.linkup.place.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.command.application.dto.request.FavoriteCreateRequest;
import com.learningcrew.linkup.place.command.application.dto.response.FavoriteCommandResponse;
import com.learningcrew.linkup.place.command.application.service.FavoriteCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "즐겨찾기 관리", description = "즐겨찾기 등록 및 제거 API")
public class
FavoriteCommandController {

    private final FavoriteCommandService favoriteCommandService;

    @Operation(
            summary = "즐겨찾기 등록",
            description = "회원이 장소를 즐겨찾기에 등록한다."
    )
    @PostMapping("/places/{placeId}")
    public ResponseEntity<ApiResponse<FavoriteCommandResponse>> createFavorite(
            @PathVariable("placeId") int placeId,
            @RequestBody @Validated FavoriteCreateRequest favoriteCreateRequest) {

        FavoriteCommandResponse response = favoriteCommandService.createFavorite(placeId, favoriteCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @Operation(
            summary = "즐겨찾기 삭제",
            description = "회원이 즐겨찾기에 있는 장소를 삭제한다."
    )
    @DeleteMapping("/places/{placeId}")
    public ResponseEntity<ApiResponse<Void>> deleteFavorite(
            @PathVariable("placeId") int placeId,
            @RequestParam("memberId") int memberId
    ) {
        favoriteCommandService.deleteFavorite(memberId, placeId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
