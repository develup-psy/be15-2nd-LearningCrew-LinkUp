package com.learningcrew.linkup.point.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "계좌 등록/수정 요청 DTO")
public class AccountRequest {

    @Schema(description = "은행명", example = "국민은행")
    @NotBlank(message = "은행명은 필수입니다. ")
    private String bankName;

    @Schema(description = "계좌번호", example = "12345678901234")
    @NotBlank(message = "계좌번호는 필수입니다. ")
    private String accountNumber;

    @Schema(description = "예금주", example = "홍길동")
    @NotBlank(message = "예금주명은 필수입니다. ")
    private String holderName;
}
