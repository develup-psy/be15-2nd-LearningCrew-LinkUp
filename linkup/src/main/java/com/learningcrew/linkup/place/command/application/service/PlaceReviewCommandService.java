package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.command.infrastructure.repository.JpaMeetingParticipationHistoryRepository;
import com.learningcrew.linkup.place.command.application.dto.request.PlaceReviewCreateRequest;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.PlaceReview;
import com.learningcrew.linkup.place.command.domain.repository.PlaceReviewRepository;
import com.learningcrew.linkup.place.query.dto.response.PlaceReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceReviewCommandService {
    private final JpaMeetingParticipationHistoryRepository meetingParticipationHistoryRepository;
    private final MeetingRepository meetingRepository;
    private final PlaceReviewRepository placeReviewRepository;

    public PlaceReviewResponse createReview(PlaceReviewCreateRequest request) {
        MeetingParticipationHistory history = meetingParticipationHistoryRepository
                .findByMemberIdAndMeetingIdAndStatusId(request.getMemberId(), request.getMeetingId(), 5)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED));

        Meeting meeting = meetingRepository.findById(request.getMeetingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));
        PlaceReview review = PlaceReview.builder()
                .memberId(request.getMemberId())
                .participationId(history.getParticipationId())
                .placeId(meeting.getPlaceId())
                .statusId(2) // 작성 상태
                .reviewContent(request.getReviewContent())
                .reviewImageUrl(request.getReviewImageUrl())
                .reviewScore(request.getReviewScore())
                .build();

        placeReviewRepository.save(review);

        return new PlaceReviewResponse(
                review.getReviewContent(),
                review.getReviewImageUrl(),
                review.getReviewScore()
        );
    }
}