package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMeetingRepository extends MeetingRepository, JpaRepository<Meeting, Integer> {

}
