package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.place.command.application.dto.request.ReservationCreateRequest;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Reservation;
import com.learningcrew.linkup.place.command.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;

    public void createReservation(ReservationCreateRequest reservationCreateRequest) {
        Reservation reservation = Reservation.builder()
                .meetingId(reservationCreateRequest.getMeetingId())
                .placeId(reservationCreateRequest.getPlaceId())
                .statusId(reservationCreateRequest.getStatusId())
                .reservationDate(reservationCreateRequest.getReservationDate())
                .startTime(reservationCreateRequest.getStartTime())
                .endTime(reservationCreateRequest.getEndTime())
                .build();

        reservationRepository.save(reservation);
    }
}
