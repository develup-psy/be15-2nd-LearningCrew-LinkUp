package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantsResponse;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingParticipationQueryService {

    private final MeetingParticipationMapper mapper;

    @Transactional(readOnly = true)
    public ParticipantsResponse getParticipants(int meetingId) {
        List<MemberDTO> response = mapper.selectParticipantsByMeetingId(meetingId);

        return ParticipantsResponse.builder()
                .participants(response)
                .build();
    }

    @Transactional(readOnly = true)
    public MeetingParticipationResponse getParticipation(int meetingId, int memberId) {
        MeetingParticipationDTO response = mapper.selectMeetingParticipationByMeetingIdAndMemberId(meetingId, memberId);

        return MeetingParticipationResponse.builder()
                .participationId(response.getParticipationId())
                .build();
    }
}
