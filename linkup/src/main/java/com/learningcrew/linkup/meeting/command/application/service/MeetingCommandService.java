package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingCommandService {

    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository participationRepository;
    private final MeetingParticipationCommandService participationCommandService;

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_ACCEPTED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_DELETED = 4;

    private static final int MIN_USER = 1;
    private static final int MAX_USER = 30;

    /** 1. 모임 생성 */
    @Transactional
    public int createMeeting(MeetingCreateRequest request) {
        validateMeetingCreateRequest(request);

        Meeting meeting = Meeting.builder()
                .leaderId(request.getLeaderId())
                .placeId(request.getPlaceId())
                .sportId(request.getSportId())
                .statusId(STATUS_PENDING)
                .meetingTitle(request.getMeetingTitle())
                .meetingContent(request.getMeetingContent())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .minUser(request.getMinUser())
                .maxUser(request.getMaxUser())
                .gender(request.getGender())
                .ageGroup(request.getAgeGroup())
                .level(request.getLevel())
                .customPlaceAddress(request.getCustomPlaceAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Meeting saved = meetingRepository.save(meeting);

        // 개설자 자동 참가 처리
        MeetingParticipationCreateRequest participationRequest = MeetingParticipationCreateRequest.builder()
                .memberId(saved.getLeaderId())
                .build();

        participationCommandService.createMeetingParticipation(participationRequest, saved);

        return saved.getMeetingId();
    }

    /** 2. 리더 변경 */
    @Transactional
    public int updateLeader(int meetingId, int newLeaderId, LeaderUpdateRequest request) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getLeaderId() != request.getMemberId()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "리더만 리더 변경 권한이 있습니다.");
        }

        List<MeetingParticipationHistory> acceptedParticipants =
                participationRepository.findByMeetingIdAndStatusId(meetingId, STATUS_ACCEPTED);

        boolean isNewLeaderParticipant = acceptedParticipants.stream()
                .anyMatch(p -> p.getMemberId() == newLeaderId);

        if (!isNewLeaderParticipant) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "해당 회원은 모임 참여자가 아닙니다.");
        }

        participationRepository.findByMeetingIdAndMemberId(meetingId, request.getMemberId())
                .ifPresent(oldLeaderHistory -> {
                    oldLeaderHistory.setStatusId(STATUS_DELETED);
                    participationRepository.save(oldLeaderHistory);
                });

        meeting.setLeaderId(newLeaderId);
        meetingRepository.save(meeting);

        return meetingId;
    }

    /** 3. 모임 삭제 */
    @Transactional
    public void deleteMeeting(int meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        // 모든 상태 참가자들을 DELETED 처리
        for (int statusId : new int[]{STATUS_PENDING, STATUS_ACCEPTED, STATUS_REJECTED}) {
            List<MeetingParticipationHistory> participants =
                    participationRepository.findByMeetingIdAndStatusId(meetingId, statusId);

            for (MeetingParticipationHistory history : participants) {
                history.setStatusId(STATUS_DELETED);
                participationRepository.save(history);
            }
        }

        meeting.setStatusId(STATUS_DELETED);
        meetingRepository.save(meeting);
    }

    /** 유효성 검사 */
    private void validateMeetingCreateRequest(MeetingCreateRequest request) {
        LocalDate meetingDate = request.getDate();
        LocalDate now = LocalDate.now();

        if (meetingDate.isBefore(now) || meetingDate.isAfter(now.plusDays(14))) {
            throw new BusinessException(ErrorCode.INVALID_MEETING_DATE_FILTER);
        }

        if (request.getStartTime().getMinute() % 30 != 0 || request.getEndTime().getMinute() % 30 != 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "시작/종료 시간은 30분 단위여야 합니다.");
        }

        int minUser = request.getMinUser();
        int maxUser = request.getMaxUser();

        if (minUser < MIN_USER || maxUser > MAX_USER || minUser > maxUser) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "유효하지 않은 인원 설정입니다.");
        }
    }
}
