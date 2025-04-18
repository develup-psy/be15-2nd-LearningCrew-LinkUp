package com.learningcrew.linkup.point.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.point.query.dto.response.AccountResponse;
import com.learningcrew.linkup.point.query.dto.response.MonthlySettlementResponse;
import com.learningcrew.linkup.point.query.dto.response.PointHistoryResponse;
import com.learningcrew.linkup.point.query.dto.response.SettlementDetailResponse;
import com.learningcrew.linkup.point.query.service.PointQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "포인트", description = "포인트 및 계좌 조회 API")
public class PointQueryController {
    private final PointQueryService pointQueryService;

    @GetMapping("/accounts")
    @Operation(summary = "계좌 조회")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @AuthenticationPrincipal String userId
    ) {
        AccountResponse response = pointQueryService.getAccount(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(response, "계좌 조회 성공"));
    }

    @Operation(summary = "포인트 내역 조회", description = "포인트 사용, 충전, 환불 내역을 조회합니다.")
    @GetMapping("/payments/me/history")
    public ResponseEntity<ApiResponse<List<PointHistoryResponse>>> getPointHistory(
            @Parameter(description = "사용자 ID", required = true) @AuthenticationPrincipal String userId
    ) {
        List<PointHistoryResponse> response = pointQueryService.getPointHistory(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(response, "포인트 내역 조회 성공"));
    }

    @Operation(summary = "전체 정산 내역 조회", description = "사업자의 모든 정산 내역을 조회합니다.")
    @GetMapping("/settlements/me")
    public ResponseEntity<ApiResponse<List<SettlementDetailResponse>>> getAllSettlements(
            @Parameter(description = "사업자 ID", required = true) @AuthenticationPrincipal String userId
    ) {
        List<SettlementDetailResponse> response = pointQueryService.getAllSettlements(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(response, "전체 정산 내역 조회 성공"));
    }

    @Operation(summary = "월별 정산 대금 조회", description = "해당 월의 정산 금액을 조회합니다.")
    @GetMapping("/settlements/me/monthly")
    public ResponseEntity<ApiResponse<MonthlySettlementResponse>> getMonthlySettlement(
            @Parameter(description = "사업자 ID", required = true) @AuthenticationPrincipal String userId,
            @Parameter(description = "조회할 연도 (yyyy)", example = "2025") @RequestParam int year,
            @Parameter(description = "조회할 월 (MM)", example = "04") @RequestParam int month
    ) {
        MonthlySettlementResponse response = pointQueryService.getMonthlySettlement(Integer.parseInt(userId), year, month);
        return ResponseEntity.ok(ApiResponse.success(response, "월별 정산 대금 조회 성공"));
    }
}
