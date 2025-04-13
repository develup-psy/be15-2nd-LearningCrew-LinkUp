package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.place.command.application.dto.request.ReservationCreateRequest;
import com.learningcrew.linkup.place.command.application.dto.response.ReservationCommandResponse;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Reservation;
import com.learningcrew.linkup.place.command.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationRepository reservationRepository;

    public ReservationCommandResponse createReservation(ReservationCreateRequest request) {
        // 겹치는 예약이 있는지 체크
        int conflictCount = reservationRepository.countConflictingReservations(
                request.getPlaceId(),
                request.getReservationDate(),
                request.getStartTime(),
                request.getEndTime()
        );
        System.out.println("겹치는 갯수 : " + conflictCount);

        int statusId;
        String message;

        if (conflictCount > 0) {
            statusId = 3;
            message = "예약이 거절되었습니다. 이미 해당 시간에 예약이 존재합니다.";
        } else {
            statusId = 2;
            message = "예약이 성공적으로 완료되었습니다.";
        }

        // 예약 엔티티 생성 및 저장
        Reservation reservation = Reservation.builder()
                .meetingId(request.getMeetingId())
                .placeId(request.getPlaceId())
                .statusId(statusId)
                .reservationDate(request.getReservationDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        // 이 시점에 INSERT가 발생하고, reservationId는 AutoIncrement 값이 된다
        reservationRepository.save(reservation);

        return new ReservationCommandResponse(
                reservation.getReservationId(),  // 반드시 DB 저장된 값
                statusId,
                message
        );
    }

}
