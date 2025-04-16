package com.learningcrew.linkup.point.command.application.controller;

import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.application.service.PointCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="포인트 충전" , description = "포인트 충전 API")
public class PointCommandController {
    private final PointCommandService pointCommandService;

    @Operation(
            summary = "포인트 충전",
            description = "회원은 포인트를 충전한다."
    )
    @PostMapping("/transaction")
    public ResponseEntity<PointTransactionResponse> createPointTransaction(
            @RequestBody PointTransactionRequest request
    ){
        PointTransactionResponse response = pointCommandService.createPointTransaction(request);
        return ResponseEntity.ok(response);
    }
}
