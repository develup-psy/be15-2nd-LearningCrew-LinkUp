package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.common.query.mapper.SportTypeMapper;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.command.infrastructure.repository.JpaMeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingMapper;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import com.learningcrew.linkup.meeting.query.service.StatusQueryService;

import com.learningcrew.linkup.notification.command.application.helper.PointNotificationHelper;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.query.service.PlaceQueryService;
import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.service.PointCommandService;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;

import com.learningcrew.linkup.notification.command.application.helper.MeetingNotificationHelper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor // 의존성 주입
public class MeetingCommandService {
    static final int MINIMUM_OF_MIN_USER = 1;
    static final int MAXIMUM_OF_MAX_USER = 30;

    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository participationRepository;
    private final ModelMapper modelMapper;
    private final MeetingMapper meetingMapper;
    private final MeetingParticipationMapper participationMapper;
    private final MeetingNotificationHelper meetingNotificationHelper;
    private final PointNotificationHelper pointNotificationHelper;

    private final MeetingParticipationCommandService commandService;
    private final MeetingParticipationCommandService participationCommandService;
    private final MeetingParticipationQueryService participationQueryService;
    private final StatusQueryService statusQueryService;
    private final SportTypeMapper sportTypeMapper;
    private final PlaceQueryService placeQueryService;
    private final PointRepository pointRepository;
    private final PointCommandService pointCommandService;
    private final UserFeignClient userFeignClient;
    private final JpaMeetingParticipationHistoryRepository jpaRepository;


    @Transactional
    public int createMeeting(MeetingCreateRequest meetingCreateRequest) {
        /* 1. 검증 로직 */
        LocalDate meetingDate = meetingCreateRequest.getDate();
        LocalDate now = LocalDate.now();

        if (meetingDate.isBefore(now) || meetingDate.isEqual(now.plusDays(15))) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        if (meetingCreateRequest.getStartTime().getMinute() % 30 != 0 || meetingCreateRequest.getEndTime().getMinute() % 30 != 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        int minUser = meetingCreateRequest.getMinUser();
        int maxUser = meetingCreateRequest.getMaxUser();

        if (maxUser < minUser) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        if (minUser < MINIMUM_OF_MIN_USER || maxUser > MAXIMUM_OF_MAX_USER) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        /* 2. 포인트 검증 및 결제 */
        Place place = placeQueryService.getPlaceById(meetingCreateRequest.getPlaceId());
        int rentalCost = place.getRentalCost();

        int leaderId = meetingCreateRequest.getLeaderId();
        int ownerId = place.getOwnerId();
        int leaderPointBalance = userFeignClient.getPointBalance(leaderId);

        int requiredPoint = rentalCost / minUser;

        if (leaderPointBalance < requiredPoint) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        pointCommandService.paymentTransaction(leaderId, requiredPoint);
        pointCommandService.payPlaceRentalCost(ownerId, rentalCost);

        /* 3. 모임 저장 */
        Meeting meeting = modelMapper.map(meetingCreateRequest, Meeting.class);
        meeting.setStatusId(statusQueryService.getStatusId("PENDING"));

        meetingRepository.save(meeting);
        meetingRepository.flush();

        Meeting savedMeeting = meetingRepository.findById(meeting.getMeetingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        /* 4. 개설자를 모임 참가자에 등록 */
        MeetingParticipationCreateRequest request = MeetingParticipationCreateRequest.builder()
                .memberId(leaderId)
                .build();

        participationCommandService.createMeetingParticipation(request, savedMeeting);

        return meeting.getMeetingId();
    }


    /* 개설자 변경 */
    @Transactional
    public int updateLeader(int meetingId, int memberId, LeaderUpdateRequest leaderUpdateRequest) {
        // 1. 모임 정보 조회
        MeetingDTO meeting = meetingMapper.selectMeetingById(meetingId);

        // 2. 권한 체크 (현재 리더가 요청한 사람인지 확인)
        if (meeting.getLeaderId() != leaderUpdateRequest.getMemberId()) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 3. 새로운 개설자가 모임 참여자인지 확인
        List<MemberDTO> participants = participationMapper.selectParticipantsByMeetingId(meetingId);
        if (participants.stream().noneMatch(p -> p.getMemberId() == memberId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "모임에 속하지 않은 회원입니다.");
        }

// 4. 기존 개설자의 참여 내역 soft delete
        MeetingParticipationDTO requestedParticipation = participationMapper.selectHistoryByMeetingIdAndMemberId(meetingId, leaderUpdateRequest.getMemberId());
        MeetingParticipationHistory origin = participationRepository.findByMeetingIdAndMemberId(meetingId, leaderUpdateRequest.getMemberId());

        MeetingParticipationHistory history = modelMapper.map(requestedParticipation, MeetingParticipationHistory.class);
        history.setParticipatedAt(origin.getParticipatedAt());
        history.setStatusId(statusQueryService.getStatusId("DELETED")); // soft delete
        participationRepository.save(history);

// 환불 처리
        commandService.cancelParticipation(meetingId, leaderUpdateRequest.getMemberId());


        // 5. 모임 리더 변경
        meeting.setLeaderId(memberId);
        Meeting meetingEntity = modelMapper.map(meeting, Meeting.class);

        int sportId = sportTypeMapper.findBySportName(meeting.getSportName())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST))
                .getSportTypeId();
        int statusId = statusQueryService.getStatusId("DELETED");

        meetingEntity.setSportId(sportId);
        meetingEntity.setStatusId(statusId);
        meetingRepository.save(meetingEntity);

        /* 개설자 변경 알림 발송 */
        meetingNotificationHelper.sendLeaderChangeNotification(
                memberId,       // 알림 받을 사람 (모임 개설자)
                meeting.getMeetingTitle()           // 모임 제목 (바인딩될 {meetingTitle})
        );

        return meetingId;
    }

    /* 모임 삭제 */
    @Transactional
    public void deleteMeeting(int meetingId) {
        cancelMeetingByLeader(meetingId);
        /* 준서 : List.of에 ACCEPTED가 있어서 4가 이미 처리가 되어서 계속 안나왔던 이유입니다.
        *  혹시나 제가 ACCPETED를 빼서 뭔가 에러가 발생한다면 수정하겠습니다. */
        for (String string : List.of("PENDING", "REJECTED")) { // history에서 status를 모두 DELETED로 변경
            List<MeetingParticipationDTO> participants = participationQueryService.getHistories(
                    meetingId, statusQueryService.getStatusId(string)
            );

            participants.forEach(commandService::deleteMeetingParticipation);
        }

        /* DTO와 Entity 간 필요한 변환 로직 */
        MeetingDTO meeting = meetingMapper.selectMeetingById(meetingId);
        meeting.setStatusType("모임 취소");

        Meeting meetingEntity = modelMapper.map(meeting, Meeting.class);
        meetingEntity.setSportId(
                sportTypeMapper.findBySportName(meeting.getSportName())
                        .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST))
                        .getSportTypeId()
        );
        /* 모임의 status를 deleted로 변경하고 update */
        meetingEntity.setStatusId(statusQueryService.getStatusId("DELETED"));
        meetingRepository.save(meetingEntity);
        List<MeetingParticipationHistory> acceptedParticipants =
                jpaRepository.findByMeetingIdAndStatusId(
                        meetingId,
                        statusQueryService.getStatusId("ACCEPTED")
                );
        if (meeting.getPlaceId() != null) {
            Place place = placeQueryService.getPlaceById(meeting.getPlaceId());
            int ownerId = place.getOwnerId();
            int rentalCost = place.getRentalCost();

            // 사업자 포인트 회수
            pointCommandService.revokePlaceRentalCost(ownerId, rentalCost);
        }


        if (!acceptedParticipants.isEmpty()) {
            Place place = placeQueryService.getPlaceById(meeting.getPlaceId());
            int rentalCost = place.getRentalCost();
            int minUser = meeting.getMinUser();
            int refundAmount = rentalCost / minUser;

            for (MeetingParticipationHistory participant : acceptedParticipants) {
                int participantId = participant.getMemberId();

                pointCommandService.refundParticipationPoint(participantId, refundAmount);
            }
        }

    }
    @Transactional
    public void validateCreatorBalance(int creatorId, Integer placeId, int minUser) {
        if (placeId == null) return; // 장소 없으면 돈 필요 없음

        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();
        int costPerUser = rentalCost / minUser;

        int currentPointBalance = userFeignClient.getPointBalance(creatorId);

        if (currentPointBalance< costPerUser) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE,
                    "개설자의 포인트가 부족합니다. 최소 필요 포인트: " + costPerUser);
        }

        userFeignClient.decreasePoint(creatorId, costPerUser);

        PointTransaction transaction = new PointTransaction(
                null,
                creatorId,
                costPerUser*(-1),
                "PAYMENT", // 또는 다른 타입으로 구분 가능 eg. "CREATOR_PAYMENT"
                null // createdAt은 DB default
        );

        int afterPointBalance = userFeignClient.getPointBalance(creatorId);

        /* 개설자 포인트 사용 알림 발송 */
        pointNotificationHelper.sendPaymentNotification(
                creatorId,
                place.getPlaceName(),
                costPerUser,
                afterPointBalance
        );

        pointRepository.save(transaction);
    }
    @Transactional
    public void cancelMeetingByLeader(int meetingId) {
        // 1. 모임 상태를 DELETED로 변경
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        meeting.setStatusId(statusQueryService.getStatusId("DELETED"));
        meetingRepository.save(meeting);

        // 2. 참여자 중 상태가 ACCEPTED인 애들만 골라서 환불
        List<MeetingParticipationHistory> acceptedParticipants =
                participationRepository.findByMeetingIdAndStatusId(
                        meetingId,
                        statusQueryService.getStatusId("ACCEPTED")
                );

        // 3. 참여자 환불 (cancelParticipation 재사용)
        for (MeetingParticipationHistory participant : acceptedParticipants) {
            commandService.cancelParticipation(meetingId, participant.getMemberId());
        }
    }


    @Transactional
    public void forceCompleteMeeting(int meetingId) {
        System.out.println("[DEBUG] 모임 강제완료 시작 - meetingId: " + meetingId);

        // 1. 모임 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        System.out.println("[DEBUG] 모임 조회 완료 - placeId: " + meeting.getPlaceId());

        // 2. ACCEPTED 참여자 조회
        List<MeetingParticipationHistory> acceptedParticipants =
                participationRepository.findByMeetingIdAndStatusId(
                        meetingId,
                        statusQueryService.getStatusId("ACCEPTED")
                );

        System.out.println("[DEBUG] 참여자 수 조회 완료 - 인원: " + acceptedParticipants.size());

        // 3. 참여자 없을 때 예외 처리 추가 (중요)
        if (acceptedParticipants.isEmpty()) {
            System.out.println("[ERROR] 참가자 없음 - 모임 완료 불가");
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가자가 존재하지 않아 모임을 완료할 수 없습니다.");
        }

        // 4. 환불/정산 계산
        int rentalCost = placeQueryService.getPlaceById(meeting.getPlaceId()).getRentalCost();
        System.out.println("[DEBUG] 장소 대여비 조회 완료 - rentalCost: " + rentalCost);

        int perPersonCost = rentalCost / acceptedParticipants.size();
        int prePaid = rentalCost / meeting.getMinUser();

        System.out.println("[DEBUG] 1인당 요금 계산 완료 - perPersonCost: " + perPersonCost + ", prePaid: " + prePaid);

        for (MeetingParticipationHistory participant : acceptedParticipants) {
            int userId = participant.getMemberId();
            System.out.println("[DEBUG] 참여자 처리 시작 - userId: " + userId);

            if (prePaid > perPersonCost) {
                int refundAmount = prePaid - perPersonCost;
                System.out.println("[DEBUG] 환불 대상 - userId: " + userId + ", refundAmount: " + refundAmount);

                // 포인트 환불
                pointCommandService.refundExtraPoint(userId, refundAmount);
            }

            participant.setStatusId(statusQueryService.getStatusId("DONE"));
            System.out.println("[DEBUG] 참여자 상태 DONE 업데이트 - userId: " + userId);
        }

        // 5. 모임 상태도 DONE 처리
        meeting.setStatusId(statusQueryService.getStatusId("DONE"));
        meetingRepository.save(meeting);

        System.out.println("[DEBUG] 모임 상태 DONE 업데이트 완료 - meetingId: " + meetingId);
    }

}
