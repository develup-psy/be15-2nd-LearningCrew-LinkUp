package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 의존성 주입
public class MeetingCommandService {

    private final MeetingRepository meetingRepository;
    private final ModelMapper modelMapper;

    /* 모임 등록 */
    @Transactional
    public int createMeeting(MeetingCreateRequest meetingCreateRequest) {
        Meeting meeting = modelMapper.map(meetingCreateRequest, Meeting.class);

        meetingRepository.save(meeting);

        return meeting.getMeetingId();
    }

    /* 모임 삭제 */
    @Transactional
    public void deleteMeeting(int meetingId) {}

    /* 주최자 변경 */
    @Transactional
    public void updateLeader(int meetingId, int memberId, int requestedMemberId) {

    }
}
