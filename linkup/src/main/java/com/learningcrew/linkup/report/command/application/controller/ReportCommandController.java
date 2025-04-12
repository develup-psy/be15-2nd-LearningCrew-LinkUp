package com.learningcrew.linkup.report.command.application.controller;

import com.learningcrew.linkup.report.command.application.dto.request.CommentReportRequest;
import com.learningcrew.linkup.report.command.application.dto.request.PostReportRequest;
import com.learningcrew.linkup.report.command.application.dto.request.UserReportRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ReportRegisterResponse;
import com.learningcrew.linkup.report.command.application.service.ReportCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
@Tag(name = "신고 등록", description = "사용자 신고 등록 API")
public class ReportCommandController {

    private final ReportCommandService reportCommandService;

    @PostMapping("/user")
    @Operation(summary = "사용자 신고", description = "사용자가 다른 사용자를 신고합니다.")
    public ResponseEntity<ReportRegisterResponse> reportUser(
            @RequestBody @Valid UserReportRequest request
    ) {
        return ResponseEntity.ok(reportCommandService.registerUserReport(request));
    }

    @PostMapping("/post")
    @Operation(summary = "게시글 신고", description = "사용자가 게시글을 신고합니다.")
    public ResponseEntity<ReportRegisterResponse> reportPost(
            @RequestBody @Valid PostReportRequest request
    ) {
        return ResponseEntity.ok(reportCommandService.registerPostReport(request));
    }

    @PostMapping("/comment")
    @Operation(summary = "댓글 신고", description = "사용자가 댓글을 신고합니다.")
    public ResponseEntity<ReportRegisterResponse> reportComment(
            @RequestBody @Valid CommentReportRequest request
    ) {
        return ResponseEntity.ok(reportCommandService.registerCommentReport(request));
    }
}

