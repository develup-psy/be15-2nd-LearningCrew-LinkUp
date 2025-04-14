package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.notification.command.application.helper.NotificationHelper;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.command.domain.repository.PlaceRepository;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingParticipationCommandService {

    private final MeetingParticipationHistoryRepository participationRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final NotificationHelper notificationHelper;
    private final PlaceRepository placeRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingStatusService meetingStatusService;

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_ACCEPTED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_DELETED = 4;
    private static final int STATUS_DONE = 5;


    /**
     * 1. 참가 신청
     */
    @Transactional
    public long createMeetingParticipation(MeetingParticipationCreateRequest request, Meeting meeting) {
        int meetingId = meeting.getMeetingId();
        int memberId = request.getMemberId();

        // 중복 참가 확인
        if (participationRepository.existsByMeetingIdAndMemberId(meetingId, memberId)) {
            throw new BusinessException(ErrorCode.MEETING_ALREADY_JOINED);
        }

        // 모임 상태 확인
        if (meeting.getStatusId() == STATUS_REJECTED
            || meeting.getStatusId() == STATUS_DELETED
            || meeting.getStatusId() == STATUS_DONE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청할 수 없는 모임입니다.");
        }

        // 시간 확인
        if (meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 시작된 모임에는 참가할 수 없습니다.");
        }

        int statusId = (meeting.getLeaderId() == memberId) ? STATUS_ACCEPTED : STATUS_PENDING;

        MeetingParticipationHistory participation = MeetingParticipationHistory.builder()
                .meetingId(meetingId)
                .memberId(memberId)
                .participatedAt(LocalDateTime.now())
                .statusId(statusId)
                .build();

        notificationHelper.sendNotification(
                meeting.getLeaderId(),  // 알림 받을 대상: 모임 개설자
                1,                  // 알림 유형 ID
                1                    // 도메인 ID
        );

        participationRepository.save(participation);
        meetingStatusService.changeStatusByMemberCount(meeting);

        return participation.getParticipationId();
    }

    /**
     * 2. 참가 승인
     */
    @Transactional
    public long acceptParticipation(Meeting meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();

        MeetingParticipationHistory participation = participationRepository
                .findByMeetingIdAndMemberId(meetingId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "참가 정보를 찾을 수 없습니다."));

        if (participation.getStatusId() != STATUS_PENDING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "승인 가능한 참가 신청이 없습니다.");
        }
        // 2. 모임이 참가 신청 승인 가능한 상태인지 확인
        // 정원 확인
        List<MeetingParticipationHistory> acceptedList =
                participationRepository.findByMeetingIdAndStatusId(meetingId, STATUS_ACCEPTED);

        if (acceptedList.size() >= meeting.getMaxUser()) {
            throw new BusinessException(ErrorCode.MEETING_PARTICIPATION_LIMIT_EXCEEDED);
        }

        if (meeting.getStatusId() == STATUS_DELETED ||
            meeting.getStatusId() == STATUS_DONE ||
            meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "모임 상태가 유효하지 않아 승인할 수 없습니다.");
        }

        payParticipation(meeting, memberId);

        // 알림 발송
        notificationHelper.sendNotification(
                participation.getMemberId(),  // 알림 받을 대상: 모임 개설자
                2,                  // 알림 유형 ID: 예) 모임 참가 신청
                1                    // 도메인 ID: 예) 모임 도메인
        );

        participationRepository.save(participation);
        meetingStatusService.changeStatusByMemberCount(meeting);
        return participation.getParticipationId();
    }

    /**
     * 3. 참가 거절
     */
    @Transactional
    public long rejectParticipation(Meeting meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();

        MeetingParticipationHistory participation = participationRepository
                .findByMeetingIdAndMemberId(meetingId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "참가 정보를 찾을 수 없습니다."));

        if (participation.getStatusId() != STATUS_PENDING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "거절 가능한 참가 신청이 없습니다.");
        }
        // 2. 모임이 참가 신청 거절 가능한 상태인지 확인
        if (meeting.getStatusId() == STATUS_DELETED) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 삭제된 모임입니다.");
        }

        // 모임 시작 시간 확인
        if (meeting.getStatusId() == STATUS_DONE ||
            meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 시작된 모임입니다.");
        }
        // 3. 참가 거절 처리
        participation.setStatusId(STATUS_REJECTED);
        participationRepository.save(participation);
        return participation.getParticipationId();
    }

    /**
     * 4. 참가 취소 (soft delete)
     */
    @Transactional
    public long deleteMeetingParticipation(MeetingParticipationHistory history) {
        if (history == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "참가 정보를 찾을 수 없습니다.");
        }

        Meeting meeting = meetingRepository.findById(history.getMeetingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getStatusId() == STATUS_DELETED) {  // soft deleted 된 모임
            throw new BusinessException(ErrorCode.MEETING_NOT_FOUND);
        }

        // 최대 인원 모집 (모임 확정) 혹은 진행 완료
        if (meeting.getStatusId() == STATUS_REJECTED || meeting.getStatusId() == STATUS_DONE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 취소가 불가능한 모임입니다.");
        }

        history.setStatusId(STATUS_DELETED);
        participationRepository.save(history);

        meetingStatusService.changeStatusByMemberCount(meeting);
        return history.getParticipationId();
    }

    /**
     * 5. 참가 가능 여부 확인 (잔액)
     */
    @Transactional(readOnly = true)
    public void validateBalance(int meetingId, int userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        validateBalance(meeting, userId);
    }

    @Transactional(readOnly = true)
    public void validateBalance(Meeting meeting, int userId) {
        int costPerUser = 0;

        // 장소가 지정된 경우만 비용 계산
        if (meeting.getPlaceId() != null) {
            Place place = placeRepository.findById(meeting.getPlaceId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

            int minUser = meeting.getMinUser();
            if (minUser <= 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "최소 인원이 1명 이상이어야 합니다.");
            }

            costPerUser = place.getRentalCost() / minUser;
        }

        // 사용자 확인 및 포인트 체크
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getPointBalance() < costPerUser) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE,
                    "포인트 잔액이 부족합니다. 최소 필요 포인트: " + costPerUser);
        }
    }


    /**
     * 포인트 차감 처리
     */
    private void payParticipation(Meeting meeting, int memberId) {
        Integer placeId = meeting.getPlaceId();
        if (placeId == null) {
            return;
        }

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        int rentalCost = place.getRentalCost();

        List<MeetingParticipationHistory> accepted =
                participationRepository.findByMeetingIdAndStatusId(meeting.getMeetingId(), STATUS_ACCEPTED);

        int numberOfParticipants = accepted.size() + 1;
        int costPerUser = rentalCost / numberOfParticipants;

        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getPointBalance() < costPerUser) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        user.subtractPointBalance(costPerUser);
        userRepository.save(user);

        PointTransaction transaction = new PointTransaction(null, memberId, costPerUser, "PAYMENT", null);
        pointRepository.save(transaction);
    }
}
