package com.learningcrew.linkup.report.query.controller;

import com.learningcrew.linkup.report.query.dto.request.BlacklistSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.BlacklistDTO;
import com.learningcrew.linkup.report.query.dto.response.BlacklistListResponse;
import com.learningcrew.linkup.report.query.service.BlacklistQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blacklist")
@RequiredArgsConstructor
@Tag(name = "블랙리스트 조회", description = "관리자 전용 블랙리스트 조회 API")
public class BlacklistQueryController {

    private final BlacklistQueryService blacklistQueryService;

    @GetMapping
    @Operation(summary = "블랙리스트 목록 조회", description = "블랙리스트 목록을 조회하고 필터링을 적용합니다.")
    public ResponseEntity<BlacklistListResponse> getBlacklists(
            @ParameterObject @ModelAttribute BlacklistSearchRequest request) {
        BlacklistListResponse response = blacklistQueryService.getBlacklists(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "블랙리스트 상세 조회", description = "블랙리스트 ID를 기준으로 상세 정보를 조회합니다.")
    public ResponseEntity<BlacklistDTO> getBlacklistById(@PathVariable Long memberId) {
        BlacklistDTO response = blacklistQueryService.getBlacklistById(memberId);
        return ResponseEntity.ok(response);
    }
}
