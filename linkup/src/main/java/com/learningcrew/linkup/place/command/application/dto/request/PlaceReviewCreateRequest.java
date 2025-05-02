package com.learningcrew.linkup.place.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceReviewCreateRequest {
    private Integer memberId;
    private Integer meetingId;
    private String reviewContent;
    private String reviewImageUrl;
    private Integer reviewScore;
}

