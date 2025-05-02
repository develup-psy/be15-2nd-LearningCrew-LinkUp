package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.command.application.dto.request.ReservationCreateRequest;
import com.learningcrew.linkup.place.command.application.dto.response.ReservationCommandResponse;
import com.learningcrew.linkup.place.command.application.service.ReservationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "외부 예약 등록", description = "사업자는 외부 예약 등록 및 삭제를 할 수 있습니다.")
public class ReservationCommandController {

    private final ReservationCommandService reservationCommandService;

    @PostMapping("/external/reservation")
    @Operation(summary = "외부 예약 등록", description = "사업자가 외부 일정을 예약합니다.")
    public ResponseEntity<ApiResponse<ReservationCommandResponse>> createExternalReservation(
            @RequestBody ReservationCreateRequest request
    ) {
        ReservationCommandResponse response = reservationCommandService.createExternalReservation(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/external/reservation/{reservationId}")
    @Operation(summary = "외부 예약 삭제", description = "사업자가 외부 예약을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteExternalReservation(@PathVariable Integer reservationId) {
        reservationCommandService.deleteExternalReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.success("삭제가 완료되었습니다."));
    }
}

