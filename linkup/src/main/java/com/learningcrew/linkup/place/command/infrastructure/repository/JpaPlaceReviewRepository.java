package com.learningcrew.linkup.place.command.infrastructure.repository;

import com.learningcrew.linkup.place.command.domain.aggregate.entity.PlaceReview;
import com.learningcrew.linkup.place.command.domain.repository.PlaceReviewRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface JpaPlaceReviewRepository extends JpaRepository<PlaceReview, Integer>, PlaceReviewRepository {
}
