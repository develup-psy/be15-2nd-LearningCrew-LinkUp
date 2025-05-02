package com.learningcrew.linkup.report.query.controller;

import com.learningcrew.linkup.report.query.dto.request.ObjectionSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.ObjectionDTO;
import com.learningcrew.linkup.report.query.dto.response.ObjectionListResponse;
import com.learningcrew.linkup.report.query.service.ObjectionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/objections")
@RequiredArgsConstructor
@Tag(name = "이의 제기 조회", description = "이의 제기 내역 조회 API")
public class ObjectionQueryController {

    private final ObjectionQueryService objectionQueryService;

    @GetMapping
    @Operation(summary = "이의 제기 목록 조회", description = "상태, 사용자 ID, 패널티 유형으로 필터링하여 이의 제기 내역을 조회한다.")
    public ResponseEntity<ObjectionListResponse> getObjections(@ParameterObject @ModelAttribute ObjectionSearchRequest request) {
        return ResponseEntity.ok(objectionQueryService.getObjections(request));
    }

    @GetMapping("/{objectionId}")
    @Operation(summary = "이의 제기 상세 조회", description = "이의 제기 ID를 기준으로 이의 제기 세부 정보를 조회한다.")
    public ResponseEntity<ObjectionDTO> getObjectionById(@PathVariable Long objectionId) {
        return ResponseEntity.ok(objectionQueryService.getObjectionById(objectionId));
    }
}
