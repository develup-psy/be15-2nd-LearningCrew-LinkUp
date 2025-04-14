package com.learningcrew.linkup.point.command.application.controller;

import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.application.service.PointCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PointCommandController {
    private final PointCommandService pointCommandService;

    @PostMapping("/transaction")
    public ResponseEntity<PointTransactionResponse> createPointTransaction(
            @RequestBody PointTransactionRequest request
    ){
        PointTransactionResponse response = pointCommandService.createPointTransaction(request);
        return ResponseEntity.ok(response);
    }
}
