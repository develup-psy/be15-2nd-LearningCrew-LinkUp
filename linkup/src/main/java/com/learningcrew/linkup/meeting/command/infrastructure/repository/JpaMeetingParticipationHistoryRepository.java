package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Primary
public interface JpaMeetingParticipationHistoryRepository extends MeetingParticipationHistoryRepository, JpaRepository<MeetingParticipationHistory, Long> {

    List<MeetingParticipationHistory> findByMeetingIdAndStatusId(int meetingId, int statusId);

    Optional<MeetingParticipationHistory> findByMeetingIdAndMemberId(int meetingId, int memberId);

    boolean existsByMeetingIdAndMemberId(int meetingId, int memberId);
    List<MeetingParticipationHistory> findByMemberIdAndMeetingIdAndStatusId(int memberId, int meetingId, int statusId);

}
