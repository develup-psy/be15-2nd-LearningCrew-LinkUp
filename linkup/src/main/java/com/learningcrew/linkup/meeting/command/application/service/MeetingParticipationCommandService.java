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
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
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

        history.setMeetingId(meeting.getMeetingId());
        history.setParticipatedAt(now);
        history.setStatusId(statusId);

        repository.save(history);
        repository.flush();

        return history.getParticipationId();
    }

    @Transactional
    public long acceptParticipation(MeetingDTO meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();
        MeetingParticipationHistory participation = jpaRepository.findByMeetingIdAndMemberId(meetingId, memberId);

        if (participation == null || participation.getStatusId() != statusQueryService.getStatusId("PENDING") ) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "승인 가능한 참가 신청 내역이 없습니다.");
        }

        // 2. 모임이 참가 신청 승인 가능한 상태인지 확인
        int participantsCount = meetingParticipationQueryService.getParticipantsByMeetingId(meetingId).size();

        // 정원 확인
        if (participantsCount >= meeting.getMaxUser()) {
            throw new BusinessException(ErrorCode.MEETING_PARTICIPATION_LIMIT_EXCEEDED);
        }

        // status 확인
        if (meeting.getStatusType().equals("모집 완료")) {
            throw new BusinessException(ErrorCode.MEETING_PARTICIPATION_LIMIT_EXCEEDED);
        }

        if (meeting.getStatusType().equals("모임 취소") || meeting.getStatusType().equals("모임 진행 완료")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청 내역을 승인할 수 없는 모임입니다.");
        }

        // 모임 시작 시간 확인
        if (meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청 내역을 승인할 수 없는 모임입니다.");
        }

        // 3. 참가 승인 처리
        participation.setStatusId(statusQueryService.getStatusId("ACCEPTED"));
        MeetingParticipationDTO dto = modelMapper.map(participation, MeetingParticipationDTO.class);
        dto.setStatusType("승인");

        repository.save(participation);
        repository.flush();

        return participation.getParticipationId();
    }

    @Transactional
    public long rejectParticipation(MeetingDTO meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();
        MeetingParticipationHistory participation = jpaRepository.findByMeetingIdAndMemberId(meetingId, memberId);
//        MeetingParticipationDTO participation = mapper.selectHistoryByMeetingIdAndMemberId(meetingId, memberId);
        if (participation == null || participation.getStatusId() != statusQueryService.getStatusId("PENDING") ) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "거절 가능한 참가 신청 내역이 없습니다.");
        }

        // 2. 모임이 참가 신청 거절 가능한 상태인지 확인
        if (meeting.getStatusType().equals("모임 취소") || meeting.getStatusType().equals("모임 진행 완료")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청 내역을 거절할 수 없는 모임입니다.");
        }

        // 모임 시작 시간 확인
        if (meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청 내역을 거절할 수 없는 모임입니다");
        }

        // 3. 참가 거절 처리
        MeetingParticipationDTO dto = modelMapper.map(participation, MeetingParticipationDTO.class);
        dto.setStatusType("거절");

        participation.setStatusId(statusQueryService.getStatusId("REJECTED"));
        repository.save(participation);
        repository.flush();

        return participation.getParticipationId();
    }

    @Transactional
    public long deleteMeetingParticipation(MeetingParticipationDTO history) {
        if (history == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "참여 정보가 없습니다.");
        }

        MeetingParticipationHistory entity = modelMapper.map(history, MeetingParticipationHistory.class);
        entity.setStatusId(statusQueryService.getStatusId("DELETED"));
        repository.save(entity);
        repository.flush();

        history.setStatusType("참가 취소"); // soft delete
        return history.getParticipationId();
    }

}


