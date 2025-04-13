package com.learningcrew.linkup.report.command.application.controller;

import com.learningcrew.linkup.report.command.application.dto.request.HandleReportRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ReportHandleResponse;
import com.learningcrew.linkup.report.command.application.service.ReportAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@Tag(name = "신고 관리", description = "관리자의 사용자 신고 관리 API")
public class ReportAdminController {

    private final ReportAdminService reportAdminService;

    @PutMapping("/{reportId}/rejected")
    @Operation(summary = "허위 신고 처리", description = "관리자가 허위 신고로 판단된 신고를 처리한다.")
    public ResponseEntity<ReportHandleResponse> markFalseReport(
            @PathVariable Long reportId
    ) {
        ReportHandleResponse response = reportAdminService.markAsFalseReport(reportId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reportId}/accepted")
    @Operation(summary = "신고 처리 완료", description = "관리자가 유효한 신고에 대해 처리 완료로 상태를 변경한다.")
    public ResponseEntity<ReportHandleResponse> handleValidReport(
            @PathVariable Long reportId,
            @RequestBody HandleReportRequest request
    ) {
        ReportHandleResponse response = reportAdminService.completeReportAndPenalize(reportId, request);
        return ResponseEntity.ok(response);
    }
}
