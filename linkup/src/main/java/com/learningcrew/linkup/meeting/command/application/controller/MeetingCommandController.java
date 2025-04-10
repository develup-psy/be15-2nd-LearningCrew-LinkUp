package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.ManageParticipationResponse;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.service.MeetingCommandService;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingCommandResponse;
import com.learningcrew.linkup.meeting.command.application.dto.response.LeaderUpdateResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingCommandController {

    private final MeetingCommandService meetingCommandService;
    private final MeetingParticipationCommandService service;
    private final MeetingParticipationQueryService participationQueryService;
    private final MeetingQueryService meetingQueryService;

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

    @PutMapping("/api/v1/meetings/{meetingId}/participation/{memberId}/accept")
    public ResponseEntity<ApiResponse<ManageParticipationResponse>> acceptParticipation(
            @PathVariable int meetingId, @PathVariable int memberId, @RequestParam int requestedMemberId
    ) {
        int leaderId = meetingQueryService.getMeeting(meetingId).getMeeting().getLeaderId();
        if (leaderId != requestedMemberId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<MeetingParticipationDTO> appliers = participationQueryService.getHistories(meetingId, 1).getMeetingParticipations();

        if (appliers.stream().noneMatch(applier -> applier.getMemberId() == memberId)) {
            return ResponseEntity.badRequest().body(ApiResponse.failure( "참가 신청하지 않은 회원입니다."));
        }

        long participationId = service.acceptParticipation(meetingId, memberId);

        ManageParticipationResponse response
                = ManageParticipationResponse.builder()
                .participationId(participationId)
                .statusId(2)
                .build();

        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PutMapping("/api/v1/meetings/{meetingId}/participation/{memberId}/reject")
    public ResponseEntity<ApiResponse<ManageParticipationResponse>> rejectParticipation(
            @PathVariable int meetingId, @PathVariable int memberId, @RequestParam int requestedMemberId
    ) {
        int leaderId = meetingQueryService.getMeeting(meetingId).getMeeting().getLeaderId();
        if (leaderId != requestedMemberId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<MeetingParticipationDTO> appliers = participationQueryService.getHistories(meetingId, 1).getMeetingParticipations();

        if (appliers.stream().noneMatch(applier -> applier.getMemberId() == memberId)) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("참가 신청하지 않은 회원입니다."));
        }

        long participationId = service.rejectParticipation(meetingId, memberId);

        ManageParticipationResponse response
                = ManageParticipationResponse.builder()
                .participationId(participationId)
                .statusId(3)
                .build();

        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PutMapping("/api/v1/meetings/{meetingId}/change-leader/{memberId}")
    public ResponseEntity<ApiResponse<LeaderUpdateResponse>> updateLeader(
            @PathVariable int meetingId, @PathVariable int memberId, @RequestParam int requestedMemberId
    ) {
        meetingCommandService.updateLeader(meetingId, memberId, requestedMemberId);
        LeaderUpdateResponse response = new LeaderUpdateResponse(meetingId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/api/v1/meetings/{meetingId}/cancel")
    public ResponseEntity<ApiResponse<MeetingCommandResponse>> deleteMeeting(
            @PathVariable int meetingId, int memberId
    ) {
        return null;
    }



}