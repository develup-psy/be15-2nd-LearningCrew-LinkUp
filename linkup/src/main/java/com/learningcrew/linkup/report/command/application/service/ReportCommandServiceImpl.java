package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.command.application.dto.request.CommentReportRequest;
import com.learningcrew.linkup.report.command.application.dto.request.PostReportRequest;
import com.learningcrew.linkup.report.command.application.dto.request.ReportStatusUpdateRequest;
import com.learningcrew.linkup.report.command.application.dto.request.UserReportRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ReportRegisterResponse;
import com.learningcrew.linkup.report.command.application.mapper.ReportRegisterMapper;
import com.learningcrew.linkup.report.command.domain.aggregate.ReportHistory;
import com.learningcrew.linkup.report.command.domain.repository.ReportHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportHistoryRepository reportRepository;
    private final ReportRegisterMapper reportRegisterMapper;

    // 사용자 신고
    @Override
    public ReportRegisterResponse registerUserReport(UserReportRequest request) {
        checkDuplicate(request.getReporterMemberId(), request.getTargetMemberId(), request.getReportTypeId(), null, null);

        ReportHistory report = ReportHistory.builder()
                .reporterId(request.getReporterMemberId())
                .targetId(request.getTargetMemberId())
                .reportTypeId(request.getReportTypeId())
                .statusId(1)
                .reason(request.getReason())
                .build();

        return saveReport(report);
    }

    // 게시글 신고
    @Override
    public ReportRegisterResponse registerPostReport(PostReportRequest request) {
        checkDuplicate(request.getReporterMemberId(), request.getTargetMemberId(), request.getReportTypeId(), request.getPostId(), null);

        ReportHistory report = ReportHistory.builder()
                .reporterId(request.getReporterMemberId())
                .targetId(request.getTargetMemberId())
                .postId(request.getPostId())
                .reportTypeId(request.getReportTypeId())
                .statusId(1)
                .reason(request.getReason())
                .build();

        return saveReport(report);
    }

    // 댓글 신고
    @Override
    public ReportRegisterResponse registerCommentReport(CommentReportRequest request) {
        checkDuplicate(request.getReporterMemberId(), request.getTargetMemberId(), request.getReportTypeId(), null, request.getCommentId());

        ReportHistory report = ReportHistory.builder()
                .reporterId(request.getReporterMemberId())
                .targetId(request.getTargetMemberId())
                .commentId(request.getCommentId())
                .reportTypeId(request.getReportTypeId())
                .statusId(1)
                .reason(request.getReason())
                .build();

        return saveReport(report);
    }

    // 신고 상태 수정
    @Override
    public void updateReportStatus(Long reportId, ReportStatusUpdateRequest request) {
        ReportHistory report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));
        report.setStatusId(request.getStatusId());
    }

    // 중복 신고 체크 공통 처리
    private void checkDuplicate(Integer reporterId, Integer targetId, Byte typeId, Integer postId, Long commentId) {
        boolean exists = reportRepository.findByReporterIdAndTargetIdAndReportTypeIdAndPostIdAndCommentId(
                reporterId, targetId, typeId, postId, commentId
        ).isPresent();

        if (exists) {
            throw new BusinessException(ErrorCode.REPORT_ALREADY_EXISTS);
        }
    }

    // 저장 및 응답 변환
    private ReportRegisterResponse saveReport(ReportHistory report) {
        ReportHistory saved = reportRepository.save(report);
        return reportRegisterMapper.toResponse(saved);
    }
}
