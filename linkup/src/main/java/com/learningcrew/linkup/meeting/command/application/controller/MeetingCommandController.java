package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.ManageParticipationRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.LeaderUpdateResponse;
import com.learningcrew.linkup.meeting.command.application.dto.response.ManageParticipationResponse;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.MeetingCommandService;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import com.learningcrew.linkup.meeting.query.service.StatusQueryService;
import com.learningcrew.linkup.place.command.application.dto.request.ReservationCreateRequest;
import com.learningcrew.linkup.place.command.application.dto.response.ReservationCommandResponse;
import com.learningcrew.linkup.place.command.application.service.ReservationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingCommandController {

    private final MeetingCommandService meetingCommandService;
    private final MeetingParticipationCommandService service;
    private final MeetingParticipationQueryService participationQueryService;
    private final MeetingQueryService meetingQueryService;
    private final ReservationCommandService reservationCommandService;
    private final StatusQueryService statusQueryService;

    @Operation(
            summary = "모임 생성",
            description = "회원이 운동 종목, 날짜(최대 2주 이내), 시간(30분 단위), 장소, 최소/최대 인원을 입력하여 모임을 개설한다."
    )
    @PostMapping("/api/v1/meetings")
    public ResponseEntity<ApiResponse<MeetingCommandResponse>> createMeeting(
            @RequestBody @Validated MeetingCreateRequest meetingCreateRequest
    ) {
        int meetingId = meetingCommandService.createMeeting(meetingCreateRequest);
        // 2. "장소 Id가 존재하면" 예약 생성
        if (meetingCreateRequest.getPlaceId() != null) {

            ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                    meetingId,
                    meetingCreateRequest.getPlaceId(),
                    java.sql.Date.valueOf(meetingCreateRequest.getDate()),
                    meetingCreateRequest.getStartTime(),
                    meetingCreateRequest.getEndTime()
            );
            ReservationCommandResponse reservationResponse = reservationCommandService.createReservation(reservationCreateRequest);
            System.out.println(reservationResponse.getMessage());
        }

        MeetingCommandResponse response = new MeetingCommandResponse(meetingId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @Operation(
            summary = "참가 승인",
            description = "개설자가 모임 신청 목록을 확인하여 참가 신청을 승인한다."
    )
    @PutMapping("/api/v1/meetings/{meetingId}/participation/{memberId}/accept")
    public ResponseEntity<ApiResponse<ManageParticipationResponse>> acceptParticipation(
            @PathVariable int meetingId, @PathVariable int memberId, @RequestBody ManageParticipationRequest manageParticipationRequest
    ) {
        // 1. 요청된 모임의 개설자가 맞는지 확인
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);

        if (meeting.getLeaderId() != manageParticipationRequest.getMemberId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 2. 참가 승인
        long participationId = service.acceptParticipation(meeting, memberId);

        ManageParticipationResponse response
                = ManageParticipationResponse.builder()
                .participationId(participationId)
                .statusType("승인")
                .build();

        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @Operation(
            summary = "참가 거절",
            description = "개설자가 모임 신청자 목록을 확인하여 참가 신청을 거절한다."
    )
    @PutMapping("/api/v1/meetings/{meetingId}/participation/{memberId}/reject")
    public ResponseEntity<ApiResponse<ManageParticipationResponse>> rejectParticipation(
            @PathVariable int meetingId, @PathVariable int memberId, @RequestBody ManageParticipationRequest manageParticipationRequest
    ) {
        // 1. 요청된 모임의 개설자가 맞는지 확인
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);

        if (meeting.getLeaderId() != manageParticipationRequest.getMemberId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 2. 참가 거절
        long participationId = service.rejectParticipation(meeting, memberId);

        ManageParticipationResponse response
                = ManageParticipationResponse.builder()
                .participationId(participationId)
                .statusType("거절")
                .build();

        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @Operation(
            summary = "개설자 참가 취소",
            description = "개설자가 다른 모임 참가자에게 개설자 권한을 넘기고 모임 참가를 취소한다."
    )
    @PutMapping("/api/v1/meetings/{meetingId}/change-leader/{memberId}")
    public ResponseEntity<ApiResponse<LeaderUpdateResponse>> updateLeader(
            @PathVariable int meetingId, @PathVariable int memberId, @RequestBody LeaderUpdateRequest leaderUpdateRequest
    ) {
        meetingCommandService.updateLeader(meetingId, memberId, leaderUpdateRequest);
        LeaderUpdateResponse response = new LeaderUpdateResponse(meetingId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "모집 취소",
            description = "개설자가 인원 모집을 취소한다."
    )
    @DeleteMapping("/api/v1/meetings/{meetingId}/cancel")
    public ResponseEntity<ApiResponse<MeetingCommandResponse>> deleteMeeting(
            @PathVariable int meetingId, @RequestParam int memberId
    ) {  /* 요청자가 개설자인지 확인 */
        int leaderId = meetingQueryService.getMeeting(meetingId)
                .getLeaderId();

        if (leaderId != memberId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        MeetingCommandResponse response = new MeetingCommandResponse(meetingId);

        meetingCommandService.deleteMeeting(meetingId);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }



}