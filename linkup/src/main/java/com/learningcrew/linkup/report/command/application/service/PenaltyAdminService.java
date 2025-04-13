package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.report.command.application.dto.request.PenaltyRequest;
import com.learningcrew.linkup.report.command.application.dto.response.PenaltyResponse;

public interface PenaltyAdminService {

    PenaltyResponse penalizePost(Integer postId, PenaltyRequest request);

    PenaltyResponse penalizeComment(Long commentId, PenaltyRequest request);

    PenaltyResponse penalizeReview(Integer reviewId, PenaltyRequest request);

    PenaltyResponse confirmReviewPenalty(Integer reviewId);

    PenaltyResponse cancelPenalty(Long penaltyId);
}
