package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import com.learningcrew.linkup.meeting.query.mapper.ParticipantReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantReviewQueryService {

    private final ParticipantReviewMapper participantReviewMapper;

    /* 평가자 ID, 피평가자 ID, 모임 ID로 리뷰 조회 */
    @Transactional(readOnly = true)
    public ParticipantReviewDTO getReview(int meetingId, int reviewerId, int revieweeId) {

        ParticipantReviewDTO participantReviewDTO = participantReviewMapper.getParticipantReview(meetingId, reviewerId, revieweeId);
        return participantReviewDTO;
    }

}
