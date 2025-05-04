package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.command.application.dto.request.ParticipantReviewCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.ParticipantReviewCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.ParticipantReviewCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "참가자 평가", description = "참가자 평가 API")
public class ParticipantReviewController {

    private final ParticipantReviewCommandService participantReviewCommandService;

    @Operation(summary = "참가자 평가", description = "회원이 진행 완료된 모임에 대하여 모임 참가자를 평가한다.")
    @PostMapping("/meetings/{meetingId}/review")
    public ResponseEntity<ApiResponse<ParticipantReviewCommandResponse>> createParticipantReview(
            @RequestBody @Validated ParticipantReviewCreateRequest request,
            @PathVariable int meetingId
    ) {
        int reviewerId = request.getReviewerId();

        ParticipantReviewCommandResponse response = participantReviewCommandService.createParticipantReview(request, reviewerId, meetingId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

}
