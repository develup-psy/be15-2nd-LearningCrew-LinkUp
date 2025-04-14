package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingSummaryDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingQueryService {

    private final MeetingMapper meetingMapper;

    /* 모임 상세 조회 */
    @Transactional(readOnly = true)
    public MeetingDTO getMeeting(int meetingId) {
        MeetingDTO meeting = meetingMapper.selectMeetingById(meetingId);
        if (meeting == null) {
            throw new BusinessException(ErrorCode.MEETING_NOT_FOUND);
        }
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
        LocalDate today = LocalDate.now();

        // 최소 날짜: 오늘 이전 불가
        if (meetingSearchRequest.getMinDate() == null) {
            meetingSearchRequest.setMinDate(today);
        }
        if (meetingSearchRequest.getMaxDate() == null) {
            meetingSearchRequest.setMaxDate(today.plusDays(14));
        }

        // 날짜 유효성 검사
        if (meetingSearchRequest.getMinDate().isBefore(today) || meetingSearchRequest.getMaxDate().isAfter(today.plusDays(14))) {
            throw new BusinessException(ErrorCode.INVALID_MEETING_DATE_FILTER);
        }

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
