package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.service.MeetingCommandService;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingCommandResponse;
import com.learningcrew.linkup.meeting.command.application.dto.response.LeaderUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MeetingCommandController {

    private final MeetingCommandService meetingCommandService;
    private final MeetingParticipationCommandService service;

    @PostMapping("/api/v1/meetings")
    public ResponseEntity<ApiResponse<MeetingCommandResponse>> createMeeting(
            @RequestBody @Validated MeetingCreateRequest meetingCreateRequest
    ) {
        int meetingId = meetingCommandService.createMeeting(meetingCreateRequest);
        MeetingCommandResponse response = new MeetingCommandResponse(meetingId);

        MeetingParticipationCreateRequest request = new MeetingParticipationCreateRequest(meetingCreateRequest.getLeaderId(), meetingId, 2, LocalDateTime.now());
        service.createMeetingParticipation(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @PutMapping("/api/v1/meetings/{meetingId}")
    public ResponseEntity<ApiResponse<LeaderUpdateResponse>> updateLeader(
            @RequestBody @Validated LeaderUpdateRequest leaderUpdateRequest,
            @PathVariable int meetingId
    ) {
        meetingCommandService.updateLeader(leaderUpdateRequest);
        LeaderUpdateResponse response = new LeaderUpdateResponse(meetingId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}