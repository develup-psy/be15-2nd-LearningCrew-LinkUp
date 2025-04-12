package com.learningcrew.linkup.place.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateRequest {
    private int meetingId;
    private int placeId;
    private int statusId;
    private Date reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
