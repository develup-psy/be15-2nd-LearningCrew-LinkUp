package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.common.domain.Status;
import com.learningcrew.linkup.common.query.mapper.StatusMapper;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.infrastructure.repository.JpaMeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import com.learningcrew.linkup.meeting.query.service.StatusQueryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor // 의존성 주입
public class MeetingParticipationCommandService {

    private final MeetingParticipationHistoryRepository repository;
    private final MeetingParticipationMapper mapper;
    private final MeetingQueryService meetingQueryService;
    private final ModelMapper modelMapper;
    private final MeetingParticipationQueryService meetingParticipationQueryService;
    private final StatusQueryService statusQueryService;
    private final JpaMeetingParticipationHistoryRepository jpaRepository;
    private StatusMapper statusMapper;

    /* 모임 참가 신청 */
    @Transactional
    public long createMeetingParticipation(MeetingParticipationCreateRequest request, Meeting meeting) {
        MeetingParticipationHistory history
                = modelMapper.map(request, MeetingParticipationHistory.class);

        LocalDateTime now = LocalDateTime.now();

        history.setMeetingId(meeting.getMeetingId());
        history.setParticipatedAt(now);

        /* 회원이 모임에 속해 있는지 확인 */
        List<Integer> participantsIds = jpaRepository.findByMeetingIdAndStatusId(
                meeting.getMeetingId(), statusQueryService.getStatusId("ACCEPTED")
        ).stream().map(MeetingParticipationHistory::getMemberId).toList();

        if (participantsIds.contains(request.getMemberId())) {
            throw new BusinessException(ErrorCode.MEETING_ALREADY_JOINED);
        }

        /* 개설 요청자가 주최자이면 ACCEPTED, 아니면 PENDING 처리 */
        int statusId;
        if (meeting.getLeaderId() != request.getMemberId()) {
            statusId = statusQueryService.getStatusId("PENDING");
        } else {
            statusId = statusQueryService.getStatusId("ACCEPTED");
        }

        /* 모임이 참가 가능한 상태인지 확인 -> pending accepted rejected deleted done 중 pending 뿐 */
        int meetingStatusId = meeting.getStatusId();
        if (meetingStatusId == statusQueryService.getStatusId("REJECTED")) {
            throw new BusinessException(ErrorCode.MEETING_PARTICIPATION_LIMIT_EXCEEDED);
        }
        if (meetingStatusId == statusQueryService.getStatusId("DELETED") || meetingStatusId == statusQueryService.getStatusId("DONE")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청할 수 없는 모임입니다.");
        } // 모임 취소 혹은 진행 완료

        /* 시간과도 비교 (모임이 종료되어야 진행 완료 처리되므로, 모임 진행 중에 신청이 가능할 수 있음) */
        LocalDateTime allowedUntil = meeting.getDate().atTime(meeting.getStartTime());
        if (allowedUntil.isBefore(now)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청할 수 없는 모임입니다.");
        }
        history.setStatusId(statusId);

        repository.save(history);

        return history.getParticipationId();
    }

    /* 개설 요청자와 모임 주최자가 일치하는지 체크 */
//        if (meeting.getLeaderId() != request.getLeaderId()) {
//        throw new BusinessException(ErrorCode.FORBIDDEN);
//    }

    @Transactional
    public long acceptParticipation(int meetingId, int memberId) {
        MeetingParticipationDTO participation = mapper.selectHistoryByMeetingIdAndMemberId(meetingId, memberId);

        participation.setStatusId(2);



        repository.save(modelMapper.map(participation, MeetingParticipationHistory.class));

        return participation.getParticipationId();
    }

    @Transactional
    public long rejectParticipation(int meetingId, int memberId) {
        MeetingParticipationDTO participation = mapper.selectHistoryByMeetingIdAndMemberId(meetingId, memberId);

        participation.setStatusId(3);

        repository.save(modelMapper.map(participation, MeetingParticipationHistory.class));

        return participation.getParticipationId();
    }

    @Transactional
    public long deleteMeetingParticipation(MeetingParticipationDTO history) {
//        if (history == null) {
//            throw new NotFoundException("참여 정보가 없습니다.");
//        }

        history.setStatusId(4); // soft delete

        repository.save(modelMapper.map(history, MeetingParticipationHistory.class));

        return history.getParticipationId();
    }

}


