package com.learningcrew.linkup.place.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "place_id", nullable = false)
    private Integer placeId;

    @Column(name = "participation_id", nullable = false)
    private Long participationId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "review_content", columnDefinition = "TEXT")
    private String reviewContent;

    @Column(name = "review_image_url", columnDefinition = "TEXT")
    private String reviewImageUrl;

    @Column(name = "review_score", nullable = false)
    private Integer reviewScore;

    // 제재 처리 시 상태 ID 업데이트 메서드
    public void updateStatus(Integer statusId) {
        this.statusId = statusId;
    }
}
