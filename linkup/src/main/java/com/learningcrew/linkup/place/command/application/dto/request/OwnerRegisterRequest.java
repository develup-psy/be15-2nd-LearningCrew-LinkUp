package com.learningcrew.linkup.place.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OwnerRegisterRequest {

    @NotBlank(message = "사업자 등록증 URL은 필수입니다.")
    private String businessRegistrationDocumentUrl;
}