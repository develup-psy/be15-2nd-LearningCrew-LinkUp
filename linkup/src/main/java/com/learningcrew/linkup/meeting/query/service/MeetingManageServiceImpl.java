package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.UserMeetingActivityResponse;
import com.learningcrew.linkup.meeting.query.mapper.AdminMeetingMapper;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingManageServiceImpl implements MeetingManageService {

    private final AdminMeetingMapper adminMeetingMapper;
    private final MeetingParticipationMapper participationMapper;

    @Override
    public MeetingListResponse getAllMeetings(MeetingSearchRequest request) {
        List<MeetingDTO> meetings = adminMeetingMapper.selectAllMeetings(request);
        long totalItems = adminMeetingMapper.countAllMeetings(request);

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalItems(totalItems)
                        .totalPage((int) Math.ceil((double) totalItems / request.getSize()))
                        .build())
                .build();
    }

    @Override
    public UserMeetingActivityResponse getUserMeetingActivity(int userId) {
        LocalDate today = LocalDate.now();

        List<MeetingDTO> pastMeetings = participationMapper.selectPastMeetingsByUserId(userId, today);
        List<MeetingDTO> upcomingMeetings = participationMapper.selectUpcomingMeetingsByUserId(userId, today);

        return UserMeetingActivityResponse.builder()
                .pastMeetings(pastMeetings)
                .upcomingMeetings(upcomingMeetings)
                .build();
    }

}
