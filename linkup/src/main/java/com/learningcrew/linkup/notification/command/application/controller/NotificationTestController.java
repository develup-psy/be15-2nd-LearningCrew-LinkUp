package com.learningcrew.linkup.notification.command.application.controller;

import com.learningcrew.linkup.notification.command.application.dto.EventNotificationRequest;
import com.learningcrew.linkup.notification.command.application.helper.NotificationHelper;
import com.learningcrew.linkup.notification.command.application.service.NotificationCommandService;
import com.learningcrew.linkup.notification.command.util.NotificationTemplateProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;


//@Tag(name = "Notification 테스트 API", description = "테스트 알림 전송용 컨트롤러")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/test")
//public class NotificationTestController {
//
//    private final NotificationCommandService notificationCommandService;
//
//    /**
//     * 테스트용 알림 전송 API
//     * 프론트 없이도 Postman으로 호출 가능
//     */
//    @Operation(summary = "테스트 알림 전송", description = "지정된 userId에게 테스트 알림을 전송합니다.")
//    @PostMapping("/alert")
//    public ResponseEntity<String> sendTestAlert(@Parameter(description = "알림 수신 유저 ID")@RequestParam Integer userId) {
//        EventNotificationRequest request = new EventNotificationRequest();
//        request.setReceiverId(userId);
//        request.setNotificationTypeId(1L); // 예시 알림 유형 ID
//        request.setDomainTypeId(2L);       // 예시 도메인 ID (예: 모임)
//
//        notificationCommandService.sendEventNotification(request);
//        return ResponseEntity.ok("✅ 테스트 알림 전송 완료 (userId = " + userId + ")");
//    }
//}
@Tag(name = "Notification 테스트 API", description = "테스트 알림 전송용 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class NotificationTestController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationHelper notificationHelper;


    /**
     * 테스트용 알림 전송 API (POST)
     * 프론트 없이도 Postman으로 호출 가능
     */
    @Operation(summary = "테스트 알림 전송", description = "테스트용 알림을 수신자에게 전송합니다.")
    @PostMapping("/alert")
    public ResponseEntity<String> sendTestAlert(@RequestBody EventNotificationRequest request) {
        notificationCommandService.sendEventNotification(request);
        return ResponseEntity.ok("✅ 테스트 알림 전송 완료 (userId = " + request.getReceiverId() + ")");
    }
}