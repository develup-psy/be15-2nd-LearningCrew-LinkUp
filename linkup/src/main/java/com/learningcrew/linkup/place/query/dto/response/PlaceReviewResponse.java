package com.learningcrew.linkup.place.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class PlaceReviewResponse {
    private String reviewContent;
    private String reviewImageUrl;
    private int reviewScore;
}
