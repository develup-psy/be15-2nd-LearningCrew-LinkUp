package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingParticipationCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import com.learningcrew.linkup.meeting.query.service.StatusQueryService;
import com.learningcrew.linkup.point.command.application.dto.response.MeetingPaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingParticipationController {
    private final MeetingParticipationCommandService service;
    private final MeetingParticipationQueryService queryService;
    private final MeetingParticipationMapper participationMapper;
    private final StatusQueryService statusQueryService;
    private final MeetingQueryService meetingQueryService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "모임 참가 신청 가능 여부 확인",
            description = "해당 모임에 참가 신청을 할 수 있는지 (포인트 잔액 기준) 확인한다."
    )
    @GetMapping("/api/v1/meetings/{meetingId}/participation/check")
    public ResponseEntity<ApiResponse<MeetingPaymentResponse>> checkEligibility(
            @PathVariable int meetingId,
            @RequestParam("userId") int userId
    ) {
        MeetingPaymentResponse response = service.checkBalance(meetingId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "모임 참가 신청",
            description = "회원이 개설된 모임에 참가를 신청한다."
    )
    @PostMapping("/api/v1/meetings/{meetingId}/participation")
    public ResponseEntity<ApiResponse<MeetingParticipationCommandResponse>> createMeetingParticipation(
            @RequestBody @Validated MeetingParticipationCreateRequest request,
            @PathVariable int meetingId
    ) {
        Meeting meeting = modelMapper.map(meetingQueryService.getMeeting(meetingId), Meeting.class);

        service.validateBalance(meetingId, request.getMemberId());


        long participationId = service.createMeetingParticipation(request, meeting);

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
            description = "회원이 참가 신청이 승인된 모임의 참가를 취소한다."
    )
    @DeleteMapping("/api/v1/meetings/{meetingId}/participation/{memberId}")
    public ResponseEntity<ApiResponse<MeetingParticipationCommandResponse>> deleteMeetingParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId
    ) { // 1. 참여자 확인
        List<MemberDTO> participants = queryService.getParticipants(meetingId, statusQueryService.getStatusId("ACCEPTED"));

        if (!participants.stream().anyMatch(x -> x.getMemberId() == (memberId) )) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 2. 참여 이력 조회
        MeetingParticipationDTO requestedParticipation = participationMapper.selectHistoryByMeetingIdAndMemberId(meetingId, memberId);

        // 3. soft delete 수행
        long participationId = service.deleteMeetingParticipation(requestedParticipation);

        // 4. 응답 반환
        MeetingParticipationCommandResponse response = MeetingParticipationCommandResponse.builder()
                .participationId(participationId)
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));

    }
}
