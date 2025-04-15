package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingSummaryDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantsResponse;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingParticipationQueryController {

    private final MeetingParticipationQueryService service;
    private final MeetingQueryService meetingQueryService;

    /* 관리자 혹은 서버 통신용 */
    @Operation(
            summary = "모임 참가자 조회",
            description = "모임 ID로 모임 참가자 목록을 조회한다."
    )
    @GetMapping("/meetings/{meetingId}/participation")
    public ResponseEntity<ApiResponse<ParticipantsResponse>> getParticipants(@PathVariable int meetingId) {
        List<MemberDTO> participants = service.getParticipantsByMeetingId(meetingId);
        ParticipantsResponse response = ParticipantsResponse.from(participants);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "참가한 모임 참가자 조회",
            description = "회원이 자신이 개설하거나 참가한 모임의 참가자 목록을 조회한다."
    )
    @GetMapping("/my-meetings/{meetingId}/participation")
    public ResponseEntity<ApiResponse<ParticipantsResponse>> getMyMeetingParticipants(@PathVariable int meetingId, @RequestParam int memberId) {
        List<MemberDTO> participants = service.getParticipantsOfMyMeeting(meetingId, memberId);

        ParticipantsResponse response = ParticipantsResponse.from(participants);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "모임 참가 이력 조회",
            description = "모임 ID, 회원 ID로 참가 내역 조회"
    )
    @GetMapping("/meetings/{meetingId}/participation/{memberId}")
    public ResponseEntity<ApiResponse<MeetingParticipationResponse>> getParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId
    ) {
        MeetingParticipationResponse response = service.getParticipation(meetingId, memberId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
