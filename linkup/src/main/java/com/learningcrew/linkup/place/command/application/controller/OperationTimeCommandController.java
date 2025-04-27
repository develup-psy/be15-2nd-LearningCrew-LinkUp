package com.learningcrew.linkup.place.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.command.application.dto.request.OperationTimeRequest;
import com.learningcrew.linkup.place.command.application.dto.response.OperationTimeUpdateResponse;
import com.learningcrew.linkup.place.command.application.service.OperationTimeCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "장소 운영시간 관리", description = "운영시간 수정 API")
public class OperationTimeCommandController
{
    private final OperationTimeCommandService operationTimeCommandService;

    @Operation(
            summary = "장소 운영시간 수정",
            description = "사업자가 자신의 장소 운영시간을 수정한다."
    )
    @PatchMapping("/place/{placeId}/times")
    public ResponseEntity<ApiResponse<OperationTimeUpdateResponse>> updateOperationTimes(
            @PathVariable int placeId,
            @RequestBody @Validated List<OperationTimeRequest> operationTimeRequests) {

        OperationTimeUpdateResponse response = operationTimeCommandService.updateOperationTimes(placeId, operationTimeRequests);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
