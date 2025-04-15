package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "모임 상세 조회", description = "모임의 조건이나 장소 등 상세 정보 조회")
@Validated
public class MeetingQueryController {

    private final MeetingQueryService meetingQueryService;
    // statusType PENDING 등 수정?

    @Operation(
            summary = "모임 상세 조회",
            description = "회원이 개설된 모임 목록에서 특정 모임의 상세 정보를 조회한다"
    )
    @GetMapping("/meetings/{meetingId}")
    public ResponseEntity<ApiResponse<MeetingDTO>> getMeetingDetails(@PathVariable int meetingId) {
        MeetingDTO response = meetingQueryService.getMeeting(meetingId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "모임 조회 (목록별, 조건별 조회)",
            description = "회원이 검색 조건을 설정하여 모임 목록을 조회한다."
    )
    @GetMapping("/meetings")
    public ResponseEntity<ApiResponse<MeetingListResponse>> getMeetingsWithPaging(
            @RequestBody MeetingSearchRequest meetingSearchRequest
    ) {
        MeetingListResponse response = meetingQueryService.getMeetings(meetingSearchRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
