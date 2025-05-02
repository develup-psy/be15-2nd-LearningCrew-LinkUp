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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceReviewCommandService {

    private final JpaMeetingParticipationHistoryRepository participationRepository;
    private final MeetingRepository meetingRepository;
    private final PlaceReviewRepository placeReviewRepository;

    private static final int STATUS_REVIEW_WRITTEN = 2;

    public PlaceReviewResponse createReview(PlaceReviewCreateRequest request) {
        // 참여 이력 먼저 조회 (status는 모든 상태 포함)
        List<MeetingParticipationHistory> participationList =
                participationRepository.findByMemberIdAndMeetingId(
                        request.getMemberId(), request.getMeetingId());

        if (participationList.isEmpty()) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "해당 모임에서 유효한 참여 기록이 없습니다.");
        }

        MeetingParticipationHistory participation = participationList.get(0);

        // 💡 중복 리뷰 작성 여부 검사
        boolean alreadyWritten = placeReviewRepository.existsByParticipationId(participation.getParticipationId());
        if (alreadyWritten) {
            throw new BusinessException(ErrorCode.REVIEW_ALREADY_WRITTEN, "이미 리뷰를 작성하셨습니다.");
        }

        // 모임 및 장소 정보 조회
        Meeting meeting = meetingRepository.findById(request.getMeetingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        // 리뷰 저장
        PlaceReview review = PlaceReview.builder()
                .memberId(request.getMemberId())
                .participationId(participation.getParticipationId())
                .placeId(meeting.getPlaceId())
                .statusId(STATUS_REVIEW_WRITTEN)
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
