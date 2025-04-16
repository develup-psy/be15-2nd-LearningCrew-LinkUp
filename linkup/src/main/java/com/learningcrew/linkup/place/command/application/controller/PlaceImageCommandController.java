package com.learningcrew.linkup.place.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.command.application.dto.request.OperationTimeRequest;
import com.learningcrew.linkup.place.command.application.dto.response.OperationTimeUpdateResponse;
import com.learningcrew.linkup.place.command.application.dto.response.PlaceImageResponse;
import com.learningcrew.linkup.place.command.application.service.PlaceImageCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "장소사진 관리", description = "장소사진 수정 API")
public class PlaceImageCommandController {

    private final PlaceImageCommandService placeImageCommandService;

    @Operation(
            summary = "장소사진 수정",
            description = "사업자가 자신의 장소사진을 수정한다."
    )
    @PutMapping("/place/{placeId}/images")
    public ResponseEntity<ApiResponse<PlaceImageResponse>> updatePlaceImages(
            @PathVariable int placeId,
            @RequestPart(name = "placeImgs", required = false) List<MultipartFile> placeImgs) {

        PlaceImageResponse response = placeImageCommandService.updatePlaceImages(placeId, placeImgs);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

