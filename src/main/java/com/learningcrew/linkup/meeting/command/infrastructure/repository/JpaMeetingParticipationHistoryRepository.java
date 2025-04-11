package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMeetingParticipationHistoryRepository extends MeetingParticipationHistoryRepository, JpaRepository<MeetingParticipationHistory, Long> {

}
