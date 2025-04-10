package com.learningcrew.linkup.report.query.controller;

import com.learningcrew.linkup.report.query.dto.request.ReportSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporterSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporteeSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.*;
import com.learningcrew.linkup.report.query.service.ReportQueryService;
import com.learningcrew.linkup.common.dto.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@Tag(name = "신고 조회", description = "관리자 전용 신고 조회 API")
public class ReportQueryController {

    private final ReportQueryService reportQueryService;

    /**
     * TEST-099: 전체 신고 내역 조회
     */
    @GetMapping
    @Operation(summary = "전체 신고 내역 조회")
    public ResponseEntity<ReportListResponse> getReports(ReportSearchRequest request) {
        return ResponseEntity.ok(reportQueryService.getReports(request));
    }

    /**
     * TEST-100: 신고 유형별 신고 내역 조회
     */
    @GetMapping("/reportType/{reportTypeId}")
    @Operation(summary = "신고 유형별 신고 내역 조회")
    public ResponseEntity<ReportListResponse> getReportsByType(@PathVariable Byte reportTypeId,
                                                                @Parameter(hidden = true) ReportSearchRequest request) {
        request.setReportTypeId(reportTypeId);
        return ResponseEntity.ok(reportQueryService.getReports(request));
    }

    /**
     * TEST-101: 누적 신고 횟수 기준 신고자 목록 조회 (페이징)
     */
    @GetMapping("/reporter-user")
    @Operation(summary = "누적 신고 횟수별 신고자 목록 조회")
    public ResponseEntity<ReportUserListResponse<ReportUserCountDTO>> getReporterListByCount(ReporterSearchRequest request) {
        return ResponseEntity.ok(reportQueryService.getReporterListByCount(request));
    }

    /**
     * TEST-102: 누적 신고 점수 기준 피신고자 목록 조회 (페이징)
     */
    @GetMapping("/reportee-user")
    @Operation(summary = "누적 신고 점수별 피신고자 목록 조회")
    public ResponseEntity<ReportUserListResponse<ReportUserScoreDTO>> getReporteeListByScore(ReporteeSearchRequest request) {
        return ResponseEntity.ok(reportQueryService.getReporteeListByScore(request));
    }

    /**
     * TEST-103: 특정 신고자에 대한 신고 내역 조회 (페이징)
     */
    @GetMapping("/reporter-user/{reporterId}")
    @Operation(summary = "신고자별 신고 내역 조회")
    public ResponseEntity<ReportListResponse> getReportsByReporter(@PathVariable Long reporterId,
                                                                    @Parameter(hidden = true) ReportSearchRequest request) {
        request.setReporterMemberId(reporterId);
        return ResponseEntity.ok(reportQueryService.getReports(request));
    }

    /**
     * TEST-104: 특정 피신고자에 대한 신고 내역 조회 (페이징)
     */
    @GetMapping("/reportee-user/{reporteeId}")
    @Operation(summary = "피신고자별 신고 내역 조회")
    public ResponseEntity<ReportListResponse> getReportsByReportee(@PathVariable Long reporteeId,
                                                                    @Parameter(hidden = true) ReportSearchRequest request) {
        request.setTargetMemberId(reporteeId);
        return ResponseEntity.ok(reportQueryService.getReports(request));
    }
}
