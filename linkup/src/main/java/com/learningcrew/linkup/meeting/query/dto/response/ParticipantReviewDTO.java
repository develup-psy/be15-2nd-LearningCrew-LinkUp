package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ParticipantReviewDTO {
    @Setter
    private long reviewId;
    private int reviewerId; // 닉네임은 중복될 수 있으므로 id 필수 (프론트 뿌려줄 때만 적절히 제외)
    private String reviewerNickname;
    private int revieweeId;
    private String revieweeNickname;
    private int meetingId;
    private int score;
    private LocalDateTime createdAt;

    public void fillNames(String reviewerNickname, String revieweeNickname) {
        this.reviewerNickname = reviewerNickname;
        this.revieweeNickname = revieweeNickname;
    }
}
