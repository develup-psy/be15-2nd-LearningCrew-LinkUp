package com.learningcrew.linkup.report.command.application.dto.response;

import com.learningcrew.linkup.report.command.domain.aggregate.PenaltyType;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PenaltyResponse {

    private Long penaltyId;

    private Integer memberId;

    private PenaltyType penaltyType;

    private Integer postId;
    private Long commentId;
    private Integer reviewId;

    private String resultMessage;
}
