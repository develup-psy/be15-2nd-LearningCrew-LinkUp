package com.learningcrew.linkup.place.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCommandResponse {
    private int reservationId;
    private int statusId;
    private String message;
}

