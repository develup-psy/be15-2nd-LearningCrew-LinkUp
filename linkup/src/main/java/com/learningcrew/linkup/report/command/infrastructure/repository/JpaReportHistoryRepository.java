package com.learningcrew.linkup.report.command.infrastructure.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.ReportHistory;
import com.learningcrew.linkup.report.command.domain.repository.ReportHistoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface JpaReportHistoryRepository extends JpaRepository<ReportHistory, Long>, ReportHistoryRepository {

    Optional<ReportHistory> findByReporterIdAndTargetIdAndReportTypeIdAndPostIdAndCommentId(
        Integer reporterId, Integer targetId, Byte reportTypeId, Integer postId, Long commentId);

    // 아래 두 메서드는 JPQL @Modifying으로 별도 구현 필요
    @Modifying
    @Query("UPDATE ReportHistory r SET r.statusId = 2 WHERE r.postId = :postId AND r.statusId <> 2")
    void updateStatusByPostId(@Param("postId") Integer postId);

    @Modifying
    @Query("UPDATE ReportHistory r SET r.statusId = 2 WHERE r.commentId = :commentId AND r.statusId <> 2")
    void updateStatusByCommentId(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE ReportHistory r SET r.statusId = 2 WHERE r.targetId = :memberId AND r.statusId <> 2")
    void markAllReportsHandledByMemberId(@Param("memberId") Integer memberId);

}
