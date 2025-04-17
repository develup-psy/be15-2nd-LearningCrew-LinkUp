package com.learningcrew.linkup.point.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.point.query.dto.response.AccountResponse;
import com.learningcrew.linkup.point.query.service.PointAccountQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "계좌", description = "계좌 Query API")
public class PointAccountQueryController {
    private final PointAccountQueryService pointAccountQueryService;

    @GetMapping
    @Operation(summary = "계좌 조회")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @AuthenticationPrincipal String userId
    ) {
        AccountResponse response = pointAccountQueryService.getAccount(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(response, "계좌 조회 성공"));
    }
}
