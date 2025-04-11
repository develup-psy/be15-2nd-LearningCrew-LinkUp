package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.ParticipantReview;
import com.learningcrew.linkup.meeting.command.domain.repository.ParticipantReviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaParticipantReviewRepository extends ParticipantReviewRepository, JpaRepository<ParticipantReview, Integer> {

}
