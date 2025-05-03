package com.learningcrew.linkup.place.command.domain.repository;

import com.learningcrew.linkup.place.command.domain.aggregate.entity.PlaceReview;

import java.util.Optional;

public interface PlaceReviewRepository {
    Optional<PlaceReview> findById(Integer reviewId);
    PlaceReview save(PlaceReview review);
    boolean existsByParticipationId(long participationId);

}
