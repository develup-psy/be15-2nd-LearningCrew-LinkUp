//package com.learningcrew.linkup.meeting.query.controller;
//
//import com.learningcrew.linkup.common.dto.ApiResponse;
//import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
//import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
//import com.learningcrew.linkup.meeting.query.dto.response.UserMeetingActivityResponse;
//import com.learningcrew.linkup.meeting.query.service.MeetingManageService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/meetings/list")
//@RequiredArgsConstructor
//@Tag(name = "관리자 - 모임 관리", description = "관리자의 모임 이력/회원별 조회 기능")
//public class AdminMeetingManageController {
//
//    private final MeetingManageService meetingManageService;
//
//    @Operation(summary = "모임 전체 목록 조회", description = "관리자가 전체 모임 목록을 필터링하여 조회한다.")
//    @GetMapping
//    public ResponseEntity<ApiResponse<MeetingListResponse>> getAllMeetings(MeetingSearchRequest request) {
//        MeetingListResponse response = meetingManageService.getAllMeetings(request);
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }
//
//    @Operation(summary = "회원 별 모임 활동 조회", description = "관리자가 특정 회원의 모임 이력과 예정 모임을 조회한다.")
//    @GetMapping("/{userId}")
//    public ResponseEntity<ApiResponse<UserMeetingActivityResponse>> getUserMeetings(@PathVariable int userId) {
//        UserMeetingActivityResponse response = meetingManageService.getUserMeetingActivity(userId);
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }
//}
