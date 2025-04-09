package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingParticipationCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
public class MeetingParticipationController {
    private final MeetingParticipationCommandService service;

    @Operation(
            summary = "모임 참가 신청",
            description = "모임에 참가를 신청한다."
    )
    @PostMapping("/api/v1/meetings/participation")
    public ResponseEntity<ApiResponse<MeetingParticipationCommandResponse>> createMeetingParticipation(
            @RequestBody @Validated MeetingParticipationCreateRequest request
    ) {

        long participationId = service.createMeetingParticipation(request);
        MeetingParticipationCommandResponse response
                = MeetingParticipationCommandResponse
                .builder()
                .participationId(participationId)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
