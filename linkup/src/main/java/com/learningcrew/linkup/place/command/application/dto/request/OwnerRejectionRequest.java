package com.learningcrew.linkup.place.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OwnerRejectionRequest {

    @NotBlank(message = "거절 사유는 필수입니다.")
    private String rejectionReason;
}