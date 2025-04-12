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
    private Integer meetingId;
    private int placeId;
    private Date reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
