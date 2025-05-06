package com.learningcrew.linkup.report.query.controller;

import com.learningcrew.linkup.report.query.dto.request.PenaltySearchRequest;
import com.learningcrew.linkup.report.query.dto.response.PenaltyDTO;
import com.learningcrew.linkup.report.query.dto.response.PenaltyListResponse;
import com.learningcrew.linkup.report.query.service.PenaltyQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/penalty")  // 전체 URL 경로 설정
@RequiredArgsConstructor
@Tag(name = "사용자 제재 조회", description = "관리자 전용 사용자 제재 내역 조회 API")
public class PenaltyQueryController {

    private final PenaltyQueryService penaltyQueryService;

    // 제재 내역 조회 (필터링 및 페이징 포함)
    @GetMapping
    @Operation(summary = "전체 제재 내역 조회", description = "상태, 사용자 ID, 패널티 유형으로 필터링하여 제재 내역을 조회한다.")
    public ResponseEntity<PenaltyListResponse> getPenalties(@ParameterObject @ModelAttribute PenaltySearchRequest request) {
        // 페이징 및 필터링된 제재 내역 조회
        PenaltyListResponse response = penaltyQueryService.getPenalties(request);
        return ResponseEntity.ok(response);  // 조회된 제재 내역과 페이징 정보 반환
    }

    // 제재 상세 조회
    @GetMapping("/{penaltyId}")
    @Operation(summary = "제재 상세 조회", description = "제재 ID를 기준으로 제재 내역을 조회한다.")
    public ResponseEntity<PenaltyDTO> getPenaltyById(@PathVariable Long penaltyId) {
        // 제재 ID를 기준으로 상세 조회
        PenaltyDTO penaltyDTO = penaltyQueryService.getPenaltyById(penaltyId);
        return ResponseEntity.ok(penaltyDTO);  // 조회된 제재 상세 내역 반환
    }

}
