package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.meeting.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDetailResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingQueryController {

    private final MeetingQueryService meetingQueryService;
//    @Controller @Schema(description=)
//
    @GetMapping("/api/v1/meetings/{meetingId}")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> getMeeting(@PathVariable int meetingId) {
        MeetingDetailResponse response = meetingQueryService.getMeeting(meetingId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/api/v1/meetings")
    public ResponseEntity<ApiResponse<MeetingListResponse>> getMeetings(
            MeetingSearchRequest meetingSearchRequest
    ) {
        MeetingListResponse response = meetingQueryService.getMeetings(meetingSearchRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
