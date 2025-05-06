package com.learningcrew.linkup.place.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminPlaceReviewDto {
    private Integer reviewId;        // 후기 ID
    private Integer memberId;       // 작성자 ID
    private String writerName;    // 작성자 닉네임
    private Integer placeId;      // 장소 ID
    private String placeName;     // 장소 이름
    private Integer score;        // 평점
    private String content;       // 후기 내용
    private String imageUrl;      // 이미지 (null 가능)
}
