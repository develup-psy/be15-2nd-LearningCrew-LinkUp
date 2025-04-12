package com.learningcrew.linkup.report.command.domain.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.ReportHistory;

import java.util.Optional;

public interface ReportHistoryRepository {

    ReportHistory save(ReportHistory report);

    Optional<ReportHistory> findById(Long id);

    Optional<ReportHistory> findByReporterIdAndTargetIdAndReportTypeIdAndPostIdAndCommentId(
            Integer reporterId,
            Integer targetId,
            Byte reportTypeId,
            Integer postId,
            Long commentId
    );

    void updateStatusByPostId(Integer postId);

    void updateStatusByCommentId(Long commentId);

    void markAllReportsHandledByMemberId(Integer memberId);

}
