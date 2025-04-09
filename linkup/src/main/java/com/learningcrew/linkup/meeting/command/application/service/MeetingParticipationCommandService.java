package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationDeleteRequest;
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

    public long deleteMeetingParticipation(MeetingParticipationDeleteRequest request) {
//        MeetingParticipationHistory history = repository.findByMemberIdAndMeetingId(
//                request.getMemberId(), request.getMeetingId()
//        ).orElseThrow(() -> new NotFoundException("참여 정보가 없습니다."));

//        history.setStatusId(request.getStatusId()); // soft delete
//
//        repository.save(history);

//        return history.getParticipationId();
        return 0;
    }
}
