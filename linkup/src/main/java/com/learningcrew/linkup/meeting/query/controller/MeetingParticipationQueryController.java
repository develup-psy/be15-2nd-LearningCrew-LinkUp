package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationResponse;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantsResponse;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingParticipationQueryController {

    private final MeetingParticipationQueryService service;

    @GetMapping("/api/v1/meetings/{meetingId}/participation")
    public ResponseEntity<ApiResponse<ParticipantsResponse>> getParticipants(@PathVariable int meetingId) {
        ParticipantsResponse response = service.getParticipants(meetingId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/api/v1/meetings/{meetingId}/participation/{memberId}")
    public ResponseEntity<ApiResponse<MeetingParticipationResponse>> getParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId
    ) {
        MeetingParticipationResponse response = service.getParticipation(meetingId, memberId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
