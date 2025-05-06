package com.learningcrew.linkup.meeting.command.application.dto.request;

import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ParticipantReviewCreateRequest {
    private int reviewerId;
    private List<ParticipantReviewDTO> reviews;
//    private int score;
}
