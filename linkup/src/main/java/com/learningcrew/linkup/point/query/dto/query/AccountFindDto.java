package com.learningcrew.linkup.point.query.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountFindDto {
    private String bankName;
    private String accountNumber;
    private String holderName;
}
