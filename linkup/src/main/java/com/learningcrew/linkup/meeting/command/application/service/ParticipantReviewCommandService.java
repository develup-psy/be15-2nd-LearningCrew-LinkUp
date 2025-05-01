package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.ParticipantReviewCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.ParticipantReviewCommandResponse;

public interface ParticipantReviewCommandService {

    ParticipantReviewCommandResponse createParticipantReview(ParticipantReviewCreateRequest request, int reviewerId, int meetingId);
}
