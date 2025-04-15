package com.learningcrew.linkup.place.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.query.dto.request.ReserveListRequest;
import com.learningcrew.linkup.place.query.dto.response.ReserveListResponse;
import com.learningcrew.linkup.place.query.service.ReservationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "사업자 예약 내역 조회", description = "장소 예약 조회 API")
public class ReservationQueryController {

    private final ReservationQueryService reservationQueryService;

    // 예약 성공 조회
    @GetMapping("/owner/{ownerId}/reserve")
    @Operation(summary = "예약 내역 조회", description = "사업자가 자신의 장소에 등록된 예약 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<ReserveListResponse>> getReserve(
            @PathVariable("ownerId") int ownerId, ReserveListRequest reserveRequest) {
        ReserveListResponse response = reservationQueryService.getReserveList(ownerId,reserveRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
