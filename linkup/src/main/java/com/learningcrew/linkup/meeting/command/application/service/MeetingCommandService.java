package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingDeleteRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationDeleteRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingMapper;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // 의존성 주입
public class MeetingCommandService {

    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository participationRepository;
    private final ModelMapper modelMapper;
    private final MeetingMapper meetingMapper;
    private final MeetingParticipationMapper participationMapper;

    private final MeetingParticipationCommandService commandService;
    private final MeetingQueryService meetingQueryService;
    private final MeetingParticipationQueryService meetingParticipationQueryService;

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
    public int updateLeader(int meetingId, int memberId, int requestedMemberId) {
        MeetingDTO meeting = meetingMapper.selectMeetingById(meetingId);
        if (meeting.getLeaderId() != requestedMemberId) {
            // TODO: 커스텀 에러
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        List<MemberDTO> participants = participationMapper.selectParticipantsByMeetingId(meetingId);
        if (participants.stream().noneMatch(participation -> participation.getMemberId() == memberId)) {
            throw new RuntimeException("모임에 속하지 않은 회원입니다.");
        }

        MeetingParticipationDeleteRequest deleteRequest = new MeetingParticipationDeleteRequest(requestedMemberId, meetingId, 4);

        commandService.deleteMeetingParticipation(deleteRequest);

        MeetingParticipationDTO participation = participationMapper.selectHistoryByMeetingIdAndMemberId(meetingId, memberId);

        participationRepository.save(modelMapper.map(participation, MeetingParticipationHistory.class));

        meeting.setLeaderId(memberId);

        meetingRepository.save(modelMapper.map(meeting, Meeting.class));

        return meetingId;
    }
}
