package com.learningcrew.linkup.meeting.query.mapper;

import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParticipantReviewMapper {
    /* 평가자 ID, 피평가자 ID, 모임 ID로 리뷰 조회 */
    ParticipantReviewDTO getParticipantReview(int meetingId, int reviewerId, int revieweeId);

}
