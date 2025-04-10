package com.learningcrew.linkup.linker.query.dto.query;

import com.learningcrew.linkup.common.constants.Gender;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class MemberProfileDTO {
    private UserProfileDTO user;
    private String nickname;
    private Gender gender;
    private String introduction;
    private BigDecimal mannerTemperature;
    private LocalDate birthdate;
    private String profileImageUrl;
}
