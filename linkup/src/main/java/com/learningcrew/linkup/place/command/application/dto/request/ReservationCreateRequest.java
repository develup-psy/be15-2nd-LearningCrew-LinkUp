package com.learningcrew.linkup.place.command.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public class ReservationCreateRequest {
    private int placeId;
    private int meetingId;
    private int statusId;
    private Date reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
