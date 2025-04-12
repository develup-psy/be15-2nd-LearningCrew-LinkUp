package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.report.command.application.dto.request.CommentReportRequest;
import com.learningcrew.linkup.report.command.application.dto.request.PostReportRequest;
import com.learningcrew.linkup.report.command.application.dto.request.ReportStatusUpdateRequest;
import com.learningcrew.linkup.report.command.application.dto.request.UserReportRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ReportRegisterResponse;

public interface ReportCommandService {

    ReportRegisterResponse registerUserReport(UserReportRequest request);

    ReportRegisterResponse registerPostReport(PostReportRequest request);

    ReportRegisterResponse registerCommentReport(CommentReportRequest request);

    void updateReportStatus(Long reportId, ReportStatusUpdateRequest request);
}
