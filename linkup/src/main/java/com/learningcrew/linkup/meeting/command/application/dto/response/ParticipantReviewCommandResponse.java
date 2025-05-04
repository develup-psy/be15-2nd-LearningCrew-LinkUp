package com.learningcrew.linkup.meeting.command.application.dto.response;

import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ParticipantReviewCommandResponse {
    List<ParticipantReviewDTO> reviews;
}
