package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationDeleteRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingParticipationCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MeetingParticipationController {
    private final MeetingParticipationCommandService service;

    @Operation(
            summary = "모임 참가 신청",
            description = "모임에 참가를 신청한다."
    )
    @PostMapping("/api/v1/meetings/{meetingId}/participation")
    public ResponseEntity<ApiResponse<MeetingParticipationCommandResponse>> createMeetingParticipation(
            @RequestBody @Validated MeetingParticipationCreateRequest request,
            @PathVariable int meetingId
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

    @Operation(
            summary = "모임 참가 취소",
            description = "모임 참가 신청을 취소한다."
    )
    @DeleteMapping("/api/v1/meetings/{meetingId}/participation/{memberId}")
    public ResponseEntity<ApiResponse<MeetingParticipationCommandResponse>> deleteMeetingParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId
    ) {
        MeetingParticipationDeleteRequest request = MeetingParticipationDeleteRequest.builder()
                .meetingId(meetingId)
                .memberId(memberId)
                .statusId(4)
                .build();

        service.deleteMeetingParticipation(request);

        MeetingParticipationCommandResponse response = MeetingParticipationCommandResponse.builder().build();

        System.out.println(request.toString());

        return ResponseEntity.ok(ApiResponse.success(response));

    }
}
