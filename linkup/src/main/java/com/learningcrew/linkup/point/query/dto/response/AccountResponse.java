package com.learningcrew.linkup.point.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "계좌 조회 응답 DTO")
public class AccountResponse {

    @Schema(description = "은행명", example = "국민은행")
    private String bankName;

    @Schema(description = "계좌번호", example = "12345678901234")
    private String accountNumber;

    @Schema(description = "예금주", example = "홍길동")
    private String holderName;
}
