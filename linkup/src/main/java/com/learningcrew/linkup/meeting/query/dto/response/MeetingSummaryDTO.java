package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MeetingSummaryDTO {
    private int meetingId;
    private String leaderNickname;
    private String placeName;
    private String placeAddress;
    private String sportName;
    private String statusType;
    private String meetingTitle;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int minUser;
    private int maxUser;
    private String gender;
    private String ageGroup;
    private String level;
    private String customPlaceAddress;

    public void convertToStatusDescription() {
        switch (statusType) {
            case "PENDING" -> statusType = "모집중";
            case "ACCEPTED" -> statusType = "최소 인원 모집 완료";
            case "REJECTED" -> statusType = "모집 완료";
            case "DELETED" -> statusType = "모임 취소";
            case "DONE" -> statusType = "모임 진행 완료";
        }
    }
}
