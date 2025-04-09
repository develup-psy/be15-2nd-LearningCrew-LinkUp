package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 의존성 주입
public class MeetingParticipationCommandService {

    private final MeetingParticipationHistoryRepository repository;
    private final ModelMapper modelMapper;

    /* 모임 등록 */
    @Transactional
    public long createMeetingParticipation(MeetingParticipationCreateRequest request) {
        MeetingParticipationHistory history = modelMapper.map(request, MeetingParticipationHistory.class);

        repository.save(history);

        return history.getParticipationId();
    }

}
