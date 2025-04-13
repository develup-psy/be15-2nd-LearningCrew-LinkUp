package com.learningcrew.linkup.report.command.application.controller;

import com.learningcrew.linkup.report.command.application.dto.request.BlacklistRegisterRequest;
import com.learningcrew.linkup.report.command.application.dto.response.BlacklistRegisterResponse;
import com.learningcrew.linkup.report.command.application.dto.response.BlacklistRemoveResponse;
import com.learningcrew.linkup.report.command.application.service.BlacklistAdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blacklist")
@RequiredArgsConstructor
public class BlacklistAdminController {

    private final BlacklistAdminService blacklistAdminService;

    @PostMapping("/{memberId}")
    @Operation(summary = "블랙리스트 등록", description = "관리자가 사용자를 블랙리스트에 등록합니다.")
    public ResponseEntity<BlacklistRegisterResponse> registerBlacklist(
            @RequestBody @Valid BlacklistRegisterRequest request
    ) {
        BlacklistRegisterResponse response = blacklistAdminService.registerBlacklist(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}/clear")
    @Operation(summary = "블랙리스트 해제", description = "관리자가 블랙리스트에서 사용자를 해제합니다.")
    public ResponseEntity<BlacklistRemoveResponse> removeBlacklist(
            @PathVariable Integer memberId
    ) {
        BlacklistRemoveResponse response = blacklistAdminService.removeBlacklist(memberId);
        return ResponseEntity.ok(response);
    }
}
