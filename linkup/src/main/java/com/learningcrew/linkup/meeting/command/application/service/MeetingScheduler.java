//package com.learningcrew.linkup.meeting.command.application.service;
//
//import com.learningcrew.linkup.exception.BusinessException;
//import com.learningcrew.linkup.exception.ErrorCode;
//import com.learningcrew.linkup.linker.command.domain.aggregate.User;
//import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
//import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
//import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
//import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
//import com.learningcrew.linkup.meeting.query.service.MeetingParticipationQueryService;
//import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
//import com.learningcrew.linkup.meeting.query.service.StatusQueryService;
//import com.learningcrew.linkup.place.query.service.PlaceQueryService;
//import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
//import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class MeetingScheduler {
//
//    private final MeetingQueryService meetingQueryService;
//    private final MeetingParticipationQueryService participationQueryService;
//    private final PlaceQueryService placeQueryService;
//    private final UserRepository userRepository;
//    private final PointRepository pointTransactionRepository;
//    private final MeetingRepository meetingRepository;
//    private final StatusQueryService statusQueryService;
//
//    @Scheduled(fixedRate = 5 * 60 * 1000) // 매 분마다 실행 (테스트 용)
//    @Transactional
//    public void settleMeetings() {
//        int statusId = statusQueryService.getStatusId("ACCEPTED");
//        LocalTime now = LocalDateTime.now().toLocalTime();
//        meetingRepository.findMeetingsToSettle(now, List.of(statusId));
//
//        // 1. 시작 시간이 지난 모임 중 DONE/DELETED 처리 안된 모임 조회
//        List<Meeting> meetings = meetingRepository.findMeetingsToSettle(now,List.of(statusId));
//
//        for (Meeting meeting : meetings) {
//            int meetingId = meeting.getMeetingId();
//            int placeId = meeting.getPlaceId();
//            int minUser = meeting.getMinUser();
//            int rentalCost = placeQueryService.getPlaceById(placeId).getRentalCost();
//
//            List<MeetingParticipationHistory> participants =
//                    participationQueryService.getAcceptedParticipants(meetingId);
//
//            if (participants.size() < minUser) {
//                cancelMeetingAndRefund(meeting, participants, rentalCost);
//            } else {
//                completeMeetingAndRefundExtra(meeting, participants, rentalCost);
//            }
//        }
//    }
//
//    private void cancelMeetingAndRefund(Meeting meeting, List<MeetingParticipationHistory> participants, int rentalCost) {
//        for (MeetingParticipationHistory p : participants) {
//            User user = userRepository.findById(p.getMemberId())
//                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//            int refundAmount = rentalCost / meeting.getMinUser();
//            user.addPointBalance(refundAmount);
//            userRepository.save(user);
//
//            pointTransactionRepository.save(new PointTransaction(null, user.getUserId(), refundAmount, "REFUND", null));
//
//            p.setStatusId(statusQueryService.getStatusId("DELETED"));
//        }
//
//        // 장소 소유자 포인트 회수
//        User owner = userRepository.findById(placeQueryService.getPlaceById(meeting.getPlaceId()).getOwnerId())
//                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//        owner.subtractPointBalance(rentalCost);
//        userRepository.save(owner);
//
//        pointTransactionRepository.save(new PointTransaction(null, owner.getUserId(), -rentalCost, "REVOKE", null));
//
//        meeting.setStatusId(statusQueryService.getStatusId("DELETED"));
//        meetingRepository.save(meeting);
//    }
//
//    private void completeMeetingAndRefundExtra(Meeting meeting, List<MeetingParticipationHistory> participants, int rentalCost) {
//        int actualPerPerson = rentalCost / participants.size();
//        int prePaidPerPerson = rentalCost / meeting.getMinUser();
//
//        for (MeetingParticipationHistory p : participants) {
//            int refundAmount = prePaidPerPerson - actualPerPerson;
//
//            if (refundAmount > 0) {
//                User user = userRepository.findById(p.getMemberId())
//                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//                user.addPointBalance(refundAmount);
//                userRepository.save(user);
//
//                pointTransactionRepository.save(new PointTransaction(null, user.getUserId(), refundAmount, "REFUND", null));
//            }
//
//            p.setStatusId(statusQueryService.getStatusId("DONE"));
//        }
//
//        meeting.setStatusId(statusQueryService.getStatusId("DONE"));
//        meetingRepository.save(meeting);
//    }
//}
