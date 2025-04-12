package com.learningcrew.linkup.report.command.application.controller;

import com.learningcrew.linkup.report.command.application.dto.response.ObjectionDecisionResponse;
import com.learningcrew.linkup.report.command.application.service.ObjectionDecisionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/objections")
@RequiredArgsConstructor
public class ObjectionDecisionController {

    private final ObjectionDecisionService objectionDecisionService;

    @PutMapping("/{objectionId}/accept")
    @Operation(summary = "이의 제기 승인", description = "관리자가 이의 제기를 승인합니다.")
    public ResponseEntity<ObjectionDecisionResponse> acceptObjection(@PathVariable Long objectionId) {
        ObjectionDecisionResponse response = objectionDecisionService.acceptObjection(objectionId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{objectionId}/reject")
    @Operation(summary = "이의 제기 거절", description = "관리자가 이의 제기를 거절합니다.")
    public ResponseEntity<ObjectionDecisionResponse> rejectObjection(@PathVariable Long objectionId) {
        ObjectionDecisionResponse response = objectionDecisionService.rejectObjection(objectionId);
        return ResponseEntity.ok(response);
    }
}
