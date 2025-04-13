package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingSummaryDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingQueryService {

    private final MeetingMapper meetingMapper;

    /* 모임 상세 조회 */
    @Transactional(readOnly = true)
    public MeetingDTO getMeeting(int meetingId) {

        MeetingDTO meeting = meetingMapper.selectMeetingById(meetingId);
        meeting.convertToStatusDescription();
        return meeting;
    }

    /* 모임 목록 조회 (페이징) */
    public MeetingListResponse getMeetings(MeetingSearchRequest meetingSearchRequest) {
        List<MeetingSummaryDTO> meetings = meetingMapper.selectMeetings(meetingSearchRequest);
        meetings.forEach(MeetingSummaryDTO::convertToStatusDescription);

        long totalItems = meetingMapper.countMeetings(meetingSearchRequest);

        int page = meetingSearchRequest.getPage();
        int size = meetingSearchRequest.getSize();

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalItems / size ))
                        .totalItems(totalItems)
                        .build())
                .build();

    }

}
