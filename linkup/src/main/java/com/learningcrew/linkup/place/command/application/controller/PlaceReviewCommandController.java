package com.learningcrew.linkup.place.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.command.application.dto.request.PlaceReviewCreateRequest;
import com.learningcrew.linkup.place.command.application.dto.response.PlaceCommandResponse;
import com.learningcrew.linkup.place.command.application.dto.response.PlaceImageResponse;
import com.learningcrew.linkup.place.command.application.service.PlaceReviewCommandService;
import com.learningcrew.linkup.place.query.dto.response.PlaceReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PlaceReviewCommandController {

    private final PlaceReviewCommandService placeReviewCommandService;

    @PostMapping("/place/{placeId}/review")
    public ResponseEntity<ApiResponse<PlaceReviewResponse>> createPlaceReview(
            @PathVariable int placeId,
            @RequestBody PlaceReviewCreateRequest placeReviewCreateRequest) {
        PlaceReviewResponse response = placeReviewCommandService.createReview(placeReviewCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
