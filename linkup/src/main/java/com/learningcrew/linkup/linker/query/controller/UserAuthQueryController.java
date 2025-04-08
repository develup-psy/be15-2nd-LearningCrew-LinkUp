package com.learningcrew.linkup.linker.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.linker.query.dto.response.UserProfileResponse;
import com.learningcrew.linkup.linker.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAuthQueryController {
    private final UserQueryService userQueryService;
    /* 프로필 조회 */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        UserProfileResponse userProfileResponse =  userQueryService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(userProfileResponse));
    }


}
