package com.learningcrew.linkup.notification.command.application.helper;

import com.learningcrew.linkup.notification.command.application.dto.EventNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointNotificationHelper {

    private final NotificationHelper notificationHelper;

    /**
     * 포인트 차감 알림
     */
    public void sendPaymentNotification(int memberId, String meetingTitle, int amountPerPerson, int pointBalance) {
        Map<String, String> variables = new HashMap<>();
        variables.put("meetingTitle", meetingTitle);
        variables.put("amountPerPerson", String.valueOf(amountPerPerson));
        variables.put("pointBalance", String.valueOf(pointBalance));

        log.info("🔔 포인트 차감 알림 바인딩 - meetingTitle: {}, amountPerPerson: {}, pointBalance: {}",
                meetingTitle, amountPerPerson, pointBalance);

        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(memberId);
        request.setNotificationTypeId(24); // 포인트 차감 알림 ID
        request.setDomainTypeId(1);        // 도메인 ID: 포인트
        request.setVariables(variables);

        log.info("📦 포인트 차감 알림 전송 - to: {}, variables: {}", memberId, variables);

        notificationHelper.sendNotification(request);
    }


    /**
     * 개설자 포인트 차감 알림
     */
    public void sendLeaderPaymentNotification(int memberId, String placeName, int costPerPerson, int pointBalance) {
        Map<String, String> variables = new HashMap<>();
        variables.put("placeName", placeName);
        variables.put("amountPerPerson", String.valueOf(costPerPerson));
        variables.put("pointBalance", String.valueOf(pointBalance));

        log.info("🔔 [개설자] 포인트 차감 알림 바인딩 - placeName: {}, amountPerPerson: {}, pointBalance: {}",
                placeName, costPerPerson, pointBalance);

        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(memberId);
        request.setNotificationTypeId(25); // 개설자 포인트 차감 알림 ID
        request.setDomainTypeId(4);        // 도메인 ID: 포인트
        request.setVariables(variables);

        log.info("📦 [개설자] 포인트 차감 알림 전송 - to: {}, variables: {}", memberId, variables);

        notificationHelper.sendNotification(request);
    }
} 