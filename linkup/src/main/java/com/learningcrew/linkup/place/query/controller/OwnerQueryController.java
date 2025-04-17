package com.learningcrew.linkup.place.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.query.dto.response.OwnerResponse;
import com.learningcrew.linkup.place.query.service.OwnerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "사업자 권한 조회", description = "Owner Query API")
public class OwnerQueryController {

    private final OwnerQueryService ownerQueryService;

    // 단건 조회
    @Operation(summary = "단일 사업자 신청 조회")
    @GetMapping("/business/{targetId}/pending")
    public ResponseEntity<ApiResponse<OwnerResponse>> getOwner(
            @PathVariable int targetId
    ) {
        OwnerResponse response = ownerQueryService.getOwnerInfo(targetId);
        return ResponseEntity.ok(ApiResponse.success(response, "사업자 정보 조회 성공"));
    }

    // 사업자 전체 조회
    @Operation(summary = "모든 사업자 조회")
    @GetMapping("/businesses/accepted")
    public ResponseEntity<ApiResponse<List<OwnerResponse>>> getAllOwners() {
        List<OwnerResponse> responseList = ownerQueryService.findAllOwners();
        return ResponseEntity.ok(ApiResponse.success(responseList, "사업자 전체 조회 성공"));
    }

    // 사업자 신청 전체 조회
    @Operation(summary = "모든 사업자 신청 조회")
    @GetMapping("/businesses/pending")
    public ResponseEntity<ApiResponse<List<OwnerResponse>>> getAllPendedOwners() {
        List<OwnerResponse> responseList = ownerQueryService.findAllPendedOwners();
        return ResponseEntity.ok(ApiResponse.success(responseList, "사업자 신청 전체 조회 성공"));
    }
}