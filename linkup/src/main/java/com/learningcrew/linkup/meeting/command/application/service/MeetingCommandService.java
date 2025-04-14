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
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingCommandService {
    private List<Meeting> cachedTodaysMeetings;

    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository meetingParticipationHistoryRepository;
    private final MeetingParticipationCommandService meetingParticipationCommandService;
    private final MeetingStatusService meetingStatusService;

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_ACCEPTED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_DELETED = 4;
    private static final int STATUS_DONE = 5;

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
        meetingStatusService.changeStatusByMemberCount(saved); // 혹시 minUser 1이면 status 바로 변경

        // 개설자 자동 참가 처리
        MeetingParticipationCreateRequest participationRequest = MeetingParticipationCreateRequest.builder()
                .memberId(saved.getLeaderId())
                .build();

        meetingParticipationCommandService.createMeetingParticipation(participationRequest, saved);

        return saved.getMeetingId();
    }

    /** 2. 개설자 변경 */
    @Transactional
    public int updateLeader(int meetingId, int newLeaderId, LeaderUpdateRequest request) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getLeaderId() != request.getMemberId()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "개설자만 개설자 변경 권한이 있습니다.");
        }

        List<MeetingParticipationHistory> acceptedParticipants =
                meetingParticipationHistoryRepository.findByMeetingIdAndStatusId(meetingId, STATUS_ACCEPTED);

        boolean isNewLeaderParticipant = acceptedParticipants.stream()
                .anyMatch(p -> p.getMemberId() == newLeaderId);

        if (!isNewLeaderParticipant) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "해당 회원은 모임 참여자가 아닙니다.");
        }

        meetingParticipationHistoryRepository.findByMeetingIdAndMemberId(meetingId, request.getMemberId())
                .ifPresent(oldLeaderHistory -> {
                    oldLeaderHistory.setStatusId(STATUS_DELETED);
                    meetingParticipationHistoryRepository.save(oldLeaderHistory);
                });

        meeting.setLeaderId(newLeaderId);
        Meeting saved = meetingRepository.save(meeting);
        meetingStatusService.changeStatusByMemberCount(saved);

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
                    meetingParticipationHistoryRepository.findByMeetingIdAndStatusId(meetingId, statusId);

            for (MeetingParticipationHistory history : participants) {
                history.setStatusId(STATUS_DELETED);
                meetingParticipationHistoryRepository.save(history);
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



    @Scheduled(cron = "0 0 0 * * *") // 자정에 한 번 오늘 모임 목록 캐싱
    public void cacheTodaysMeetings() {
        LocalDate today = LocalDate.now();
        cachedTodaysMeetings = meetingRepository.findAllByDate(today);
        log.info("오늘 모임 {}건 캐싱 완료", cachedTodaysMeetings.size());
    }

    @Transactional
    @Scheduled(cron = "0 0,30 * * * *") // 매 30분마다 상태 갱신 시도
    public void updateMeetingStatuses() {
        if (cachedTodaysMeetings == null) {
            log.warn("오늘 모임 목록이 캐싱되지 않았습니다.");
            return;
        }

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        List<Meeting> updatedMeetings = new ArrayList<>();

        for (Meeting meeting : cachedTodaysMeetings) {
            LocalDateTime startAt = meeting.getDate().atTime(meeting.getStartTime());
            Duration diff = Duration.between(startAt, now);

            if (Math.abs(diff.toMinutes()) <= 1) { // 1분 차이까지 허용 (더 적게?)
                continue;
            }

            List<MeetingParticipationHistory> histories =
                    meetingParticipationHistoryRepository.findAllByMeetingIdAndStatusId(meeting.getMeetingId(), STATUS_ACCEPTED);

            if (histories.size() < meeting.getMinUser()) {
                histories.forEach(h -> h.setStatusId(STATUS_DELETED));
                meeting.setStatusId(STATUS_DELETED);
            }

            updatedMeetings.add(meeting);
        }

        if (!updatedMeetings.isEmpty()) {
            updatedMeetings.forEach(meetingRepository::save);
            log.info("총 {}건의 모임 상태 변경 완료", updatedMeetings.size());
        } else {
            log.info("변경할 모임이 없습니다.");
        }
    }

}
