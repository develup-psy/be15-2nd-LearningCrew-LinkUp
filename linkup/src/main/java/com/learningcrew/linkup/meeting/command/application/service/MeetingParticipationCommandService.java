package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.common.domain.Status;
import com.learningcrew.linkup.common.query.mapper.StatusMapper;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.domain.aggregate.Member;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.MemberRepository;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.infrastructure.repository.JpaMeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import com.learningcrew.linkup.meeting.query.service.StatusQueryService;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.query.service.PlaceQueryService;
import com.learningcrew.linkup.point.command.application.dto.response.MeetingPaymentResponse;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
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
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private StatusMapper statusMapper;
    private final PlaceQueryService placeQueryService;
    private final PointRepository pointRepository;

    @Transactional(readOnly = true)
    public MeetingPaymentResponse checkBalance(int meetingId, int userId) {
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);
        int placeId = meeting.getPlaceId();

        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int costPerUser = rentalCost / minUser;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        boolean hasEnoughPoint = user.getPointBalance() >= costPerUser;

        String message = hasEnoughPoint
                ? "참가 신청이 가능합니다."
                : "포인트 잔액이 부족합니다. 최소 필요 포인트: " + costPerUser;

        return new MeetingPaymentResponse(message, user.getPointBalance());
    }

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

    @Transactional
    public long acceptParticipation(MeetingDTO meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();
        MeetingParticipationDTO participation = mapper.selectHistoryByMeetingIdAndMemberId(meetingId, memberId);
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
        if (meeting.getStatusType().equals("REJECTED")) {
            throw new BusinessException(ErrorCode.MEETING_PARTICIPATION_LIMIT_EXCEEDED);
        }

        if (meeting.getStatusType().equals("DELETED") || meeting.getStatusType().equals("DONE")) {
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
        int statusId = statusQueryService.getStatusId("ACCEPTED");
        participation.setStatusId(statusId);

        repository.save(modelMapper.map(participation, MeetingParticipationHistory.class));

        return participation.getParticipationId();
    }

    private PointTransactionResponse payParticipation(int meetingId, int memberId) {
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);
        int placeId = meeting.getPlaceId();
        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();

        List<MemberDTO> participants = meetingParticipationQueryService.getParticipants(
                meetingId, statusQueryService.getStatusId("ACCEPTED"));
        int numberOfParticipants = participants.size() + 1; // 새로 승인될 사람 포함
        int amountPerPerson = rentalCost / numberOfParticipants;

        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 포인트 부족 검사
        if (user.getPointBalance() < amountPerPerson) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE, "포인트가 부족합니다.");
        }

        user.subtractPointBalance(amountPerPerson);
        userRepository.save(user);

        PointTransaction transaction = new PointTransaction(
                null,
                memberId,
                amountPerPerson,
                "PAYMENT",
                null
        );
        pointRepository.save(transaction);

        return new PointTransactionResponse("결제가 완료되었습니다.", user.getPointBalance());
    }

    @Transactional
    public long rejectParticipation(MeetingDTO meeting, int memberId) {
        // 1. 참가 내역 조회
        int meetingId = meeting.getMeetingId();
        MeetingParticipationDTO participation = mapper.selectHistoryByMeetingIdAndMemberId(meetingId, memberId);
        if (participation == null || participation.getStatusId() != statusQueryService.getStatusId("PENDING") ) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "거절 가능한 참가 신청 내역이 없습니다.");
        }

        // 2. 모임이 참가 신청 거절 가능한 상태인지 확인
        if (meeting.getStatusType().equals("DELETED") || meeting.getStatusType().equals("DONE")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청 내역을 거절할 수 없는 모임입니다.");
        }

        // 모임 시작 시간 확인
        if (meeting.getDate().atTime(meeting.getStartTime()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가 신청 내역을 거절할 수 없는 모임입니다");
        }

        // 3. 참가 거절 처리
        int statusId = statusQueryService.getStatusId("REJECTED");
        participation.setStatusId(statusId);

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

    @Transactional(readOnly = true)
    public void validateBalance(int meetingId, int userId) {
        MeetingDTO meeting = meetingQueryService.getMeeting(meetingId);
        int placeId = meeting.getPlaceId();

        Place place = placeQueryService.getPlaceById(placeId);
        int rentalCost = place.getRentalCost();
        int minUser = meeting.getMinUser();
        int costPerUser = rentalCost / minUser;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getPointBalance() < costPerUser) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE,
                    "포인트 잔액이 부족합니다. 최소 필요 포인트: " + costPerUser);
        }
    }

}


