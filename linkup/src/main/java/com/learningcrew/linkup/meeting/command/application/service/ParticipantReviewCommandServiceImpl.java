package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.ParticipantReviewCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.ParticipantReviewCommandResponse;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.aggregate.ParticipantReview;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.ParticipantReviewRepository;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantReviewCommandServiceImpl implements ParticipantReviewCommandService {

    private static final int REVIEW_DUE_DAY = 1;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 5;
    private static final int STATUS_DONE = 5;

    private final ParticipantReviewRepository participantReviewRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository participationRepository;

    @Transactional
    public ParticipantReviewCommandResponse createParticipantReview(ParticipantReviewCreateRequest request, int reviewerId, int meetingId) {

        // 1. 모임 존재 여부 (아예 3번과 합쳐서 status 5를 받아와도 될 듯)
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        // 2. 참가 완료된 사람들만 리뷰 가능 (STATUS_DONE = 5)
        List<MeetingParticipationHistory> doneParticipants =
                participationRepository.findByMeetingIdAndStatusId(meetingId, STATUS_DONE);

        // 3. 요청자가 참가자인지 확인
        boolean isParticipated = participationRepository.existsByMeetingIdAndMemberId(meetingId, reviewerId);
        if (!isParticipated) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        List<Integer> participantIds = doneParticipants.stream()
                .map(MeetingParticipationHistory::getMemberId)
                .filter(x -> !x.equals(reviewerId)) // 자기 자신은 평가 불가
                .toList();

        // 4. 모임이 종료 상태인지 확인
        if (meeting.getStatusId() != STATUS_DONE) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "모임이 종료되지 않았습니다.");
        }

        // 5. 작성 기한 확인
        LocalDateTime deadline = meeting.getDate().plusDays(REVIEW_DUE_DAY).atTime(meeting.getEndTime());
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "평가 가능 기간이 지났습니다.");
        }

        // 6. 중복 리뷰 확인
        boolean isAlreadyReviewed = participantReviewRepository.existsByMeetingIdAndReviewerId(
                meetingId, reviewerId);
        if (isAlreadyReviewed) {
            throw new BusinessException(ErrorCode.REVIEW_ALREADY_EXISTS, "이미 평가 등록된 모임입니다.");
        }

        List<ParticipantReviewDTO> reviews = request.getReviews();

        reviews.forEach(review -> {
            // 점수 유효성
            int score = review.getScore();
            if (score < MIN_SCORE || score > MAX_SCORE) {
                throw new BusinessException(ErrorCode.INVALID_REVIEW_SCORE);
            }

            // 리뷰 대상 유효성
            int revieweeId = review.getRevieweeId();
            if (!participantIds.contains(revieweeId)) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "평가 대상이 모임에 참여하지 않았습니다.");
            }

            ParticipantReview reviewEntity = ParticipantReview.builder()
                    .meetingId(meetingId)
                    .reviewerId(reviewerId)
                    .revieweeId(revieweeId)
                    .score(score)
                    .createdAt(LocalDateTime.now())
                    .build();

            ParticipantReview savedReview = participantReviewRepository.save(reviewEntity);
            review.setReviewId(savedReview.getReviewId());
        });
;
        return ParticipantReviewCommandResponse.builder()
                .reviews(reviews)
                .build();
    }

}
