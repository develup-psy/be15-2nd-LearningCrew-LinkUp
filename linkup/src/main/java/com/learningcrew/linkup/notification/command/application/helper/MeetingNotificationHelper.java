package com.learningcrew.linkup.notification.command.application.helper;

import com.learningcrew.linkup.notification.command.application.dto.EventNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingNotificationHelper {

    private final NotificationHelper notificationHelper;

    /**
     * 모임 예약 생성 알림 (사업자용)
     */
    public void sendReservationCreatedNotification(int ownerId, String placeName, Date reservationDate, LocalTime startTime, LocalTime endTime) {
        Map<String, String> variables = new HashMap<>();
        variables.put("placeName", placeName);
        variables.put("reservationDate", reservationDate.toString());
        variables.put("startTime", startTime.toString());
        variables.put("endTime", endTime.toString());

        log.info("🔔 예약 생성 알림 바인딩 - placeName: {}, reservationDate: {}, startTime: {}, endTime: {}",
                placeName, reservationDate, startTime, endTime);

        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(ownerId);
        request.setNotificationTypeId(5); // 예약 생성 알림 ID
        request.setDomainTypeId(2);        // 예약 도메인 ID
        request.setVariables(variables);

        log.info("📦 예약 생성 알림 전송 - to: {}, variables: {}", ownerId, variables);

        notificationHelper.sendNotification(request);
    }


    /**
     * 모임 참가 신청 알림
     */
    public void sendParticipationRequestNotification(int leaderId, String meetingTitle) {
        Map<String, String> variables = new HashMap<>();
        variables.put("meetingTitle", meetingTitle);

        log.info("🔔 모임 참가 신청 알림 바인딩 - meetingTitle: {}", meetingTitle);

        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(leaderId);
        request.setNotificationTypeId(1); // 모임 참가 신청 알림 ID
        request.setDomainTypeId(1);       // 모임 도메인 ID
        request.setVariables(variables);

        log.info("📦 모임 참가 신청 알림 전송 - to: {}, variables: {}", leaderId, variables);

        notificationHelper.sendNotification(request);
    }

    /**
     * 모임 참가 승인 알림
     */
    public void sendParticipationAcceptNotification(int memberId, String meetingTitle) {
        Map<String, String> variables = new HashMap<>();
        variables.put("meetingTitle", meetingTitle);

        log.info("🔔 모임 참가 승인 알림 바인딩 - meetingTitle: {}", meetingTitle);

        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(memberId);
        request.setNotificationTypeId(2); // 참가 승인 알림 ID
        request.setDomainTypeId(1);
        request.setVariables(variables);

        log.info("📦 모임 참가 승인 알림 전송 - to: {}, variables: {}", memberId, variables);

        notificationHelper.sendNotification(request);
    }

    /**
     * 모임 참가 거절 알림
     */
    public void sendParticipationRejectNotification(int memberId, String meetingTitle) {
        Map<String, String> variables = new HashMap<>();
        variables.put("meetingTitle", meetingTitle);

        log.info("🔔 모임 참가 거절 알림 바인딩 - meetingTitle: {}", meetingTitle);

        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(memberId);
        request.setNotificationTypeId(3); // 참가 거절 알림 ID
        request.setDomainTypeId(1);
        request.setVariables(variables);

        log.info("📦 모임 참가 거절 알림 전송 - to: {}, variables: {}", memberId, variables);

        notificationHelper.sendNotification(request);
    }

    /**
     * 개설자 변경 알림
     */
    public void sendLeaderChangeNotification(int memberId, String meetingTitle) {
        Map<String, String> variables = new HashMap<>();
        variables.put("meetingTitle", meetingTitle);

        log.info("🔔 모임 참가 신청 알림 바인딩 - meetingTitle: {}", meetingTitle);

        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(memberId);
        request.setNotificationTypeId(7); // 모임 참가 신청 알림 ID
        request.setDomainTypeId(1);       // 모임 도메인 ID
        request.setVariables(variables);

        log.info("📦 모임 참가 신청 알림 전송 - to: {}, variables: {}", memberId, variables);

        notificationHelper.sendNotification(request);
    }

} 