package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.meeting.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.request.InterestedMeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.InterestedMeetingListResponse;
import com.learningcrew.linkup.meeting.query.service.InterestedMeetingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InterestedMeetingQueryController {

    private final InterestedMeetingQueryService service;
//    @Controller @Schema(description=)
//

    @GetMapping("/api/v1/meetings/{meetingId}/interested")
    public ResponseEntity<ApiResponse<InterestedMeetingListResponse>> getInterestedMeetings(
            InterestedMeetingSearchRequest request
    ) {
        InterestedMeetingListResponse response = service.getInterestedMeetings(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
