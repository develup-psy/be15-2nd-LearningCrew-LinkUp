package com.learningcrew.linkup.place.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.command.application.dto.request.OwnerRegisterRequest;
import com.learningcrew.linkup.place.command.application.dto.request.OwnerRejectionRequest;
import com.learningcrew.linkup.place.command.application.service.OwnerCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OwnerCommandController {

    private final OwnerCommandService ownerCommandService;

    /* 사업자 등록 신청 (등록증 URL 저장) */
    @PostMapping("/business")
    public ResponseEntity<ApiResponse<Void>> registerOwner(
            @AuthenticationPrincipal String userId,
            @RequestBody OwnerRegisterRequest request
    ) {
        ownerCommandService.registerBusiness(Integer.parseInt(userId), request);
        return ResponseEntity.ok(ApiResponse.success(null,"사업자 등록 신청 완료"));
    }

    /* 사업자 권한 승인 */
    @PutMapping("/admin/businesses/{targetId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveOwner(
            @PathVariable int targetId
    ) {
        ownerCommandService.approveBusiness(targetId);
        return ResponseEntity.ok(ApiResponse.success(null,"사업자 권한 승인 완료"));
    }

    /* 사업자 권한 거절 */
    @PutMapping("/admin/businesses/{targetId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectOwner(
            @PathVariable int targetId,
            @RequestBody OwnerRejectionRequest request
    ) {
        ownerCommandService.rejectBusiness(targetId, request.getRejectionReason());
        return ResponseEntity.ok(ApiResponse.success(null,"사업자 권한 거절 처리 완료"));
    }
}
