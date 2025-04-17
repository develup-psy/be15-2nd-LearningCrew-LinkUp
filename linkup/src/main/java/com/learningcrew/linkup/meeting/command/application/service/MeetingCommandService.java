package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.domain.aggregate.Member;
import com.learningcrew.linkup.linker.command.domain.repository.MemberRepository;
import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.*;
import com.learningcrew.linkup.meeting.command.domain.repository.BestPlayerRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.ParticipantReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingCommandService {
    static final int MANNER_TEMPERATURE_SUBTRACT = 2;
    static final int MANNER_TEMPERATURE_MULTIPLIER_FOR_LEADER = 2;

    private List<Meeting> cachedTodaysMeetings;
    private List<Meeting> cachedYesterdaysMeetings;

    private final BestPlayerRepository bestPlayerRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository meetingParticipationHistoryRepository;
    private final MeetingParticipationCommandService meetingParticipationCommandService;
    private final MeetingStatusService meetingStatusService;
    private final ParticipantReviewRepository participantReviewRepository;


    private static final int STATUS_PENDING = 1;
    private static final int STATUS_ACCEPTED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_DELETED = 4;
    private static final int STATUS_DONE = 5;

    private static final int MIN_USER = 1;
    private static final int MAX_USER = 30;

    /**
     * 1. 모임 생성
     */
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

    /**
     * 2. 개설자 변경
     */
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

    /**
     * 3. 모임 삭제
     */
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

    /**
     * 유효성 검사
     */
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
        LocalDate yesterday = today.minusDays(1);

        cachedTodaysMeetings = meetingRepository.findAllByDate(today);
        cachedYesterdaysMeetings = meetingRepository.findAllByDate(yesterday);

        log.info("오늘 모임 {}건 캐싱 완료", cachedTodaysMeetings.size());
        log.info("어제 모임 {}건 캐싱 완료", cachedYesterdaysMeetings.size());
    }

    @Transactional
    @Scheduled(cron = "0 0,30 * * * *") // 매 30분마다 상태 갱신 시도
    public void updateMeetingStatuses() {
        if (cachedTodaysMeetings == null) {
            log.warn("오늘 모임 목록이 캐싱되지 않았습니다.");
            return;
        }

        LocalTime currentTime = LocalTime.now();

        cancelMeetingsByParticipantsCount(currentTime);
        updateFinishedMeetings(currentTime);
    }

    private void cancelMeetingsByParticipantsCount(LocalTime currentTime) {
        List<Meeting> updatedMeetings = new ArrayList<>();
        List<Meeting> meetings = cachedTodaysMeetings.stream()
                .filter(meeting ->
                        Math.abs(Duration.between(meeting.getStartTime(), currentTime).toMinutes()) <= 1
                ).toList();

        meetings.forEach(meeting -> {
            List<MeetingParticipationHistory> histories
                    = meetingParticipationHistoryRepository.findAllByMeetingIdAndStatusId(meeting.getMeetingId(), STATUS_ACCEPTED);

            if (histories.size() < meeting.getMinUser()) {
                histories.forEach(h -> h.setStatusId(STATUS_DELETED));
                meeting.setStatusId(STATUS_DELETED);
                updatedMeetings.add(meeting);
            }
        });

        if (!updatedMeetings.isEmpty()) {
            updatedMeetings.forEach(meetingRepository::save);
            log.info("총 {}건의 모임 상태 변경 완료", updatedMeetings.size());
        } else {
            log.info("변경할 모임이 없습니다.");
        }
    }

    private void updateFinishedMeetings(LocalTime currentTime) {
        List<Meeting> finishedMeetings = new ArrayList<>(); // 종료 처리할 모임

        List<Meeting> meetings = cachedTodaysMeetings.stream()
                .filter(meeting ->
                        Math.abs(Duration.between(meeting.getStartTime(), currentTime).toMinutes()) <= 1
                )
                .filter(meeting -> meeting.getStatusId() == STATUS_ACCEPTED || meeting.getStatusId() == STATUS_REJECTED)
                .toList(); // 최소 인원 모집 완료, 최대 인원 모집 완료된 모임을 종료 처리

        meetings.forEach(meeting -> {
            List<MeetingParticipationHistory> finishedHistories
                    = meetingParticipationHistoryRepository.findAllByMeetingId(meeting.getMeetingId())
                    .stream()
                    .filter(history -> history.getStatusId() == STATUS_ACCEPTED)
                    .toList();

            finishedHistories.forEach(h -> h.setStatusId(STATUS_DONE));
            meeting.setStatusId(STATUS_DONE);
            finishedMeetings.add(meeting);
        });

        if (!finishedMeetings.isEmpty()) {
            finishedMeetings.forEach(meetingRepository::save);
            log.info("총 {}건의 모임 상태 변경 완료", finishedMeetings.size());
        } else {
            log.info("변경할 모임이 없습니다.");
        }
    }


    @Transactional
    @Scheduled(cron = "0 0,30 * * * *")
        // 매 30분마다 반영 시도
        /* 매너온도 반영 */
    void updateMannerTemperatures() {
        if (cachedYesterdaysMeetings == null) {
            log.warn("어제 모임 목록이 캐싱되지 않았습니다.");
            return;
        }

        /* 어제 모임 중 현재 시각과 종료 시각이 일치하는 종료된(STATUS 5) 모임의 ID만 필터링 */
        List<Meeting> meetings = cachedYesterdaysMeetings.stream()
                .filter(meeting
                        -> Math.abs(Duration.between(LocalTime.now(), meeting.getEndTime()).toMinutes()) <= 1)
                .filter(meeting -> meeting.getStatusId() == STATUS_DONE)
                .toList();

        meetings.forEach(
                meeting -> {
                    Map<Member, Double> changesOfMannerTemperatures
                            = getChangesOfMannerTemperatures(meeting);

                    Double maxAvg = changesOfMannerTemperatures.values()
                            .stream().max(Comparator.naturalOrder())
                            .orElse(null);

                    if (maxAvg != null) {
                        List<Member> bestPlayers = changesOfMannerTemperatures.entrySet()
                                .stream()
                                .filter(kv -> kv.getValue().equals(maxAvg))
                                .map(Map.Entry::getKey)
                                .toList();

                        /* 베스트 플레이어 등록 */
                        bestPlayers.forEach(
                                member -> {
                                    BestPlayerId id = new BestPlayerId(meeting.getMeetingId(), member.getMemberId());
                                    BestPlayer bestPlayer = new BestPlayer(id);

                                    bestPlayerRepository.save(bestPlayer);
                                }
                        );

                        /* 리더의 변화량은 2배 */
                        int leaderId = meeting.getLeaderId();
                        Member leader = changesOfMannerTemperatures
                                .keySet().stream()
                                .filter(member -> member.getMemberId() == leaderId)
                                .findFirst().orElse(null);

                        changesOfMannerTemperatures.put(leader,
                                MANNER_TEMPERATURE_MULTIPLIER_FOR_LEADER * changesOfMannerTemperatures.get(leader)
                        );

                        changesOfMannerTemperatures.keySet()
                                .forEach(
                                        member -> {
                                            BigDecimal updatedMannerTemperature
                                                    = member.getMannerTemperature().add(BigDecimal.valueOf(changesOfMannerTemperatures.get(member)
                                            ));

                                            member.setMannerTemperature(updatedMannerTemperature);

                                            memberRepository.save(member);
                                        }
                                );
                    }
                }
        );
    }

    /* 매너온도 계산 */
    private Map<Member, Double> getChangesOfMannerTemperatures(Meeting meeting) {
        List<ParticipantReview> reviews = participantReviewRepository.findAllByMeetingId(meeting.getMeetingId());

        /* avg 편하게 계산할 수 있게 double로 */
        Map<Member, Double> changesOfMannerTemperatures = new HashMap<>();

        List<Member> reviewees = reviews.stream().map(ParticipantReview::getRevieweeId)
                .distinct()
                .map(id -> memberRepository.findById(id)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
                )
                .toList();

        reviewees.forEach(reviewee -> {
            double changeOfMannerTemperature =
                    reviews.stream()
                            .filter(review -> review.getRevieweeId() == reviewee.getMemberId())
                            .map(ParticipantReview::getScore)
                            .mapToDouble(score -> score)
                            .average()
                            .orElse(MANNER_TEMPERATURE_SUBTRACT) - MANNER_TEMPERATURE_SUBTRACT;

            changesOfMannerTemperatures.put(reviewee, changeOfMannerTemperature);
        });

        return changesOfMannerTemperatures;
    }
}
