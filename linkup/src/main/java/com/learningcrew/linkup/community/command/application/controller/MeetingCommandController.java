package com.learningcrew.linkup.community.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.ManageParticipationRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.LeaderUpdateResponse;
import com.learningcrew.linkup.meeting.command.application.dto.response.ManageParticipationResponse;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.MeetingCommandService;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.place.command.application.dto.request.ReservationCreateRequest;
import com.learningcrew.linkup.place.command.application.service.ReservationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
@Tag(name = "모임 관리", description = "모임 개설자 기능 API")
public class MeetingCommandController {

    private final MeetingCommandService meetingCommandService;
    private final MeetingParticipationCommandService participationService;
    private final ReservationCommandService reservationCommandService;
    private final MeetingRepository meetingRepository;

    @Operation(summary = "모임 생성", description = "회원이 운동 종목, 날짜(2주 이내), 시간, 장소, 인원을 입력해 모임을 개설한다.")
    @PostMapping
    public ResponseEntity<ApiResponse<MeetingCommandResponse>> createMeeting(
            @RequestBody @Validated MeetingCreateRequest request
    ) {
        int meetingId = meetingCommandService.createMeeting(request);

        // 장소가 있을 경우 예약도 함께 생성
        if (request.getPlaceId() != null) {
            ReservationCreateRequest reservationRequest = new ReservationCreateRequest(
                    meetingId,
                    request.getPlaceId(),
                    java.sql.Date.valueOf(request.getDate()),
                    request.getStartTime(),
                    request.getEndTime()
            );
            reservationCommandService.createReservation(reservationRequest);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(new MeetingCommandResponse(meetingId)));
    }

    @Operation(summary = "참가 승인", description = "개설자가 참가 신청을 승인한다.")
    @PutMapping("/{meetingId}/participation/{memberId}/accept")
    public ResponseEntity<ApiResponse<ManageParticipationResponse>> acceptParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId,
            @RequestBody ManageParticipationRequest request
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getLeaderId() != request.getMemberId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        long participationId = participationService.acceptParticipation(meeting, memberId);

        return ResponseEntity.ok(ApiResponse.success(
                ManageParticipationResponse.builder()
                        .participationId(participationId)
                        .statusType("승인")
                        .build()
        ));
    }

    @Operation(summary = "참가 거절", description = "개설자가 참가 신청을 거절한다.")
    @PutMapping("/{meetingId}/participation/{memberId}/reject")
    public ResponseEntity<ApiResponse<ManageParticipationResponse>> rejectParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId,
            @RequestBody ManageParticipationRequest request
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getLeaderId() != request.getMemberId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        long participationId = participationService.rejectParticipation(meeting, memberId);

        return ResponseEntity.ok(ApiResponse.success(
                ManageParticipationResponse.builder()
                        .participationId(participationId)
                        .statusType("거절")
                        .build()
        ));
    }

    @Operation(summary = "개설자 변경", description = "리더가 권한을 다른 참여자에게 넘긴다.")
    @PutMapping("/{meetingId}/change-leader/{memberId}")
    public ResponseEntity<ApiResponse<LeaderUpdateResponse>> updateLeader(
            @PathVariable int meetingId,
            @PathVariable int memberId,
            @RequestBody LeaderUpdateRequest request
    ) {
        meetingCommandService.updateLeader(meetingId, memberId, request);
        return ResponseEntity.ok(ApiResponse.success(new LeaderUpdateResponse(meetingId)));
    }

    @Operation(summary = "모집 취소", description = "개설자가 모임을 취소한다.")
    @DeleteMapping("/{meetingId}/cancel")
    public ResponseEntity<ApiResponse<MeetingCommandResponse>> deleteMeeting(
            @PathVariable int meetingId,
            @RequestParam int memberId
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getLeaderId() != memberId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        meetingCommandService.deleteMeeting(meetingId);
        return ResponseEntity.ok(ApiResponse.success(new MeetingCommandResponse(meetingId)));
    }
}
