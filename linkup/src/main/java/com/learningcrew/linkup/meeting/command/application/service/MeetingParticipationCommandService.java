package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.infrastructure.repository.JpaMeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import com.learningcrew.linkup.meeting.query.service.StatusQueryService;
import com.learningcrew.linkup.notification.command.application.helper.MeetingNotificationHelper;
import com.learningcrew.linkup.notification.command.application.helper.PointNotificationHelper;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.query.service.PlaceQueryService;
import com.learningcrew.linkup.point.command.application.dto.response.MeetingPaymentResponse;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // 의존성 주입
public class MeetingParticipationCommandService {

    private final MeetingParticipationHistoryRepository repository;
    private final MeetingQueryService meetingQueryService;
    private final ModelMapper modelMapper;
    private final MeetingParticipationQueryService meetingParticipationQueryService;
    private final StatusQueryService statusQueryService;
    private final MeetingNotificationHelper meetingNotificationHelper;
    private final PointNotificationHelper pointNotificationHelper;
    private final JpaMeetingParticipationHistoryRepository jpaRepository;
    private final PlaceQueryService placeQueryService;
    private final PointRepository pointRepository;
    private final UserFeignClient userFeignClient;

    @Transactional(readOnly = true)
    public MeetingPaymentResponse checkBalance(int meetingId, int userId) {
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);
        int placeId = meeting.getPlaceId();

        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int costPerUser = rentalCost / minUser;

        // 3. 사용자 포인트 조회 (Remote via FeignClient)
        int pointBalance = userFeignClient.getPointBalance(userId);

        boolean hasEnoughPoint = pointBalance >= costPerUser;

        String message = hasEnoughPoint
                ? "참가 신청이 가능합니다."
                : "포인트 잔액이 부족합니다. 최소 필요 포인트: " + costPerUser;

        return new MeetingPaymentResponse(message, pointBalance);
    }

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

        /* 참가 신청 요청자가 개설자이면 ACCEPTED, 아니면 PENDING 처리 */
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

        /* 참가 신청 알림 발송 */
        if(meeting.getLeaderId() != request.getMemberId()){
        meetingNotificationHelper.sendParticipationRequestNotification(
                meeting.getLeaderId(),       // 알림 받을 사람 (모임 개설자)
                meeting.getMeetingTitle()           // 모임 제목 (바인딩될 {meetingTitle})
        );
        }


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
        try {
            payParticipation(meetingId, memberId);
        } catch (BusinessException e) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE, "포인트 부족으로 참가 승인을 할 수 없습니다.");
        }

        // 3. 참가 승인 처리
        participation.setStatusId(statusQueryService.getStatusId("ACCEPTED"));
        MeetingParticipationDTO dto = modelMapper.map(participation, MeetingParticipationDTO.class);
        dto.setStatusId(2);

        repository.save(participation);
        repository.flush();


        /* 참가 승인 알림 발송 */
        meetingNotificationHelper.sendParticipationAcceptNotification(
                memberId,       // 알림 받을 사람 (모임 개설자)
                meeting.getMeetingTitle()           // 모임 제목 (바인딩될 {meetingTitle})
        );



        return participation.getParticipationId();
    }

    private PointTransactionResponse payParticipation(int meetingId, int memberId) {
        // 모임 조회
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);
        Integer placeId = meeting.getPlaceId();

        int pointBalance = userFeignClient.getPointBalance(memberId);

        // 2. 장소 정보 없을 경우 → 메시지만 반환
        if(placeId == null) {
            return new PointTransactionResponse("결제할 장소가 없는 모임입니다.", pointBalance);
        }

        // 장소 요금 계산
        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int amountPerPerson = rentalCost / minUser;

        // 4. 사용자 포인트 조회 (Feign)
        if (pointBalance < amountPerPerson) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE, "포인트가 부족합니다.");
        }

        // 사용자 포인트 차감 요청
        userFeignClient.decreasePoint(memberId, amountPerPerson); // 예외는 fallbackFactory로

        PointTransaction transaction = new PointTransaction(
                null,
                memberId,
                -amountPerPerson,
                "PAYMENT",
                null
        );
        pointRepository.save(transaction);

        /* 모임 신청자 포인트 사용 알림 발송 */
        pointNotificationHelper.sendPaymentNotification(
                memberId,
                meeting.getMeetingTitle(),
                amountPerPerson,
                pointBalance
        );

        return new PointTransactionResponse("결제가 완료되었습니다.", pointBalance);
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
        dto.setStatusId(3);

        participation.setStatusId(statusQueryService.getStatusId("REJECTED"));
        repository.save(participation);
        repository.flush();


        /* 참가 거절 알림 발송 */
        meetingNotificationHelper.sendParticipationRejectNotification(
                memberId,       // 알림 받을 사람 (모임 개설자)
                meeting.getMeetingTitle()           // 모임 제목 (바인딩될 {meetingTitle})
        );

        return participation.getParticipationId();
    }

    @Transactional
    public long deleteMeetingParticipation(MeetingParticipationDTO history) {
        if (history == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "참여 정보가 없습니다.");
        }

        int acceptedStatusId = statusQueryService.getStatusId("ACCEPTED");

        // JPA를 통해 참여 기록 재조회하여 상태 확인
        MeetingParticipationHistory participation = jpaRepository.findByMeetingIdAndMemberId(history.getMeetingId(), history.getMemberId());
        System.out.println("✅ 현재 상태 ID: " + participation.getStatusId());
        System.out.println("✅ ACCEPTED ID: " + acceptedStatusId);

        if (participation.getStatusId() != acceptedStatusId) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "환불 가능한 참가 정보가 아닙니다.");
        }

        // 취소 로직 실행
        cancelParticipation(history.getMeetingId(), history.getMemberId());

        // 상태 DELETED로 변경 후 저장
        participation.setStatusId(statusQueryService.getStatusId("DELETED"));
        repository.save(participation);
        repository.flush();

        history.setStatusId(statusQueryService.getStatusId("DELETED")); // soft delete
        return history.getParticipationId();
    }


    @Transactional(readOnly = true)
    public void validateBalance(int meetingId, int userId) {
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);
        Integer placeId = meeting.getPlaceId();
        if(placeId == null) {
            return;
        }

        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int costPerUser = rentalCost / minUser;

        int currentPointBalance = userFeignClient.getPointBalance(userId);

        if (currentPointBalance < costPerUser) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE,
                    "포인트 잔액이 부족합니다. 최소 필요 포인트: " + costPerUser);
        }
    }
    @Transactional
    public void cancelParticipation(int meetingId, int memberId) {
        // 1. 참여 기록 확인
        MeetingParticipationHistory participation = jpaRepository.findByMeetingIdAndMemberId(meetingId, memberId);
        if (participation == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참여 기록이 없습니다.");
        }

        int acceptedId = statusQueryService.getStatusId("ACCEPTED");
        System.out.println(acceptedId);
        System.out.println(participation.getStatusId());


        // 2. 환불 금액 계산
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);
        Integer placeId = meeting.getPlaceId();
        if (placeId == null) return; // 장소 없으면 환불 불필요

        Place place = placeQueryService.getPlaceById(placeId);
        int refundAmount = place.getRentalCost() / meeting.getMinUser();

        // 3. 포인트 환불
        userFeignClient.increasePoint(memberId, refundAmount);

        // 4. 환불 기록
        PointTransaction transaction = new PointTransaction(
                null,
                memberId,
                refundAmount,
                "REFUND",
                null
        );
        pointRepository.save(transaction);

        // 5. 참여 기록 상태 변경
        participation.setStatusId(statusQueryService.getStatusId("DELETED"));
        repository.save(participation);
    }

}


