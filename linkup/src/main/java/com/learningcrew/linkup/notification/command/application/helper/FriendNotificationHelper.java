package com.learningcrew.linkup.notification.command.application.helper;

import com.learningcrew.linkup.linker.query.service.UserQueryServiceImpl;
import com.learningcrew.linkup.notification.command.application.dto.EventNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendNotificationHelper {

    private final UserQueryServiceImpl userQueryService;
    private final NotificationHelper notificationHelper;

    /**
     * 친구 신청 알림 발송 - jgh 스타일 유지
     */
    public void sendFriendRequestNotification(int requesterId, int addresseeId) {
        /* 친구 신청 알림 발송 - jgh */
        String userName = String.valueOf(userQueryService.getUserName(requesterId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")));

        Map<String, String> variables = new HashMap<>();
        variables.put("userName", userName);

        // ✅ 로그 출력
        log.info("🔔 알림 바인딩 준비 - userName: {}", userName);

        EventNotificationRequest notificationRequest = new EventNotificationRequest();
        notificationRequest.setReceiverId(addresseeId);
        notificationRequest.setNotificationTypeId(12); // 친구 요청 알림 ID
        notificationRequest.setDomainTypeId(1);        // 도메인 ID: 친구
        notificationRequest.setVariables(variables);

        // ✅ 전체 변수 로그
        log.info("📦 알림 전송 요청 - receiverId: {}, typeId: {}, domainId: {}, variables: {}",
                addresseeId, 12, 1, variables);

        notificationHelper.sendNotification(notificationRequest);
    }


    // 친구 수락 알림
    public void sendFriendAcceptNotification(int accepterId, int requesterId) {
        String userName = String.valueOf(userQueryService.getUserName(accepterId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")));

        Map<String, String> variables = new HashMap<>();
        variables.put("userName", userName);

        log.info("🔔 친구 수락 알림 바인딩 준비 - userName: {}", userName);

        EventNotificationRequest notificationRequest = new EventNotificationRequest();
        notificationRequest.setReceiverId(requesterId);
        notificationRequest.setNotificationTypeId(13); // 친구 수락 알림 ID
        notificationRequest.setDomainTypeId(1);
        notificationRequest.setVariables(variables);

        log.info("📦 친구 수락 알림 전송 요청 - to: {}, from: {}, variables: {}",
                requesterId, accepterId, variables);

        notificationHelper.sendNotification(notificationRequest);
    }

    // 친구 삭제 알림
    public void sendFriendDeleteNotification(int deleterId, int deletedUserId) {
        String userName = String.valueOf(userQueryService.getUserName(deleterId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")));

        Map<String, String> variables = new HashMap<>();
        variables.put("userName", userName);

        log.info("🔔 친구 삭제 알림 바인딩 준비 - userName: {}", userName);

        EventNotificationRequest notificationRequest = new EventNotificationRequest();
        notificationRequest.setReceiverId(deletedUserId);
        notificationRequest.setNotificationTypeId(14); // 친구 삭제 알림 ID
        notificationRequest.setDomainTypeId(1);
        notificationRequest.setVariables(variables);

        log.info("📦 친구 삭제 알림 전송 요청 - to: {}, from: {}, variables: {}",
                deletedUserId, deleterId, variables);

        notificationHelper.sendNotification(notificationRequest);
    }
}

