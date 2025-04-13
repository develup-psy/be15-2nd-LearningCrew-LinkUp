package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationResponse;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantsResponse;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meetings")
@Tag(name = "모임 참가자 조회", description = "참여하는 모임의 참가자 목록 조회")
public class MeetingParticipationQueryController {

    private final MeetingParticipationQueryService service;

    @Operation(
            summary = "모임 참가자 조회",
            description = "모임 ID로 모임 참가자 목록을 조회한다."
    )
    @GetMapping("/{meetingId}/participation")
    public ResponseEntity<ApiResponse<ParticipantsResponse>> getParticipants(@PathVariable int meetingId) {
        ParticipantsResponse response = service.getParticipants(meetingId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "모임 참가 이력 조회",
            description = "모임 ID, 회원 ID로 참가 내역 조회"
    )
    @GetMapping("/{meetingId}/participation/{memberId}")
    public ResponseEntity<ApiResponse<MeetingParticipationResponse>> getParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId
    ) {
        MeetingParticipationResponse response = service.getParticipation(meetingId, memberId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "회원 모임 이력 조회", description = "회원이 본인의 모임 참여 이력을 조회한다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<MeetingParticipationListResponse>> getUserMeetingHistory(
            @PathVariable int userId
    ) {
        MeetingParticipationListResponse response = service.getUserMeetingHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
