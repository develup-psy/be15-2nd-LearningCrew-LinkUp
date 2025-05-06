package com.learningcrew.linkup.place.query.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminPlaceDto {
    private Integer placeId;        // 내부 링크 등에서 필요하므로 유지
    private String placeName;       // 장소 이름
    private String address;         // 주소
    private Integer ownerId;        // 사업자 번호
    private String ownerName;       // 사업자 이름
    private String sportName;       // 운동 종목 이름
    private Double averageScore;    // 평균 평점
    private Boolean isActive;
}
