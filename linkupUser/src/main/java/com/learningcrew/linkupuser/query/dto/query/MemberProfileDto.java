package com.learningcrew.linkupuser.query.dto.query;

import com.learningcrew.linkupuser.command.domain.constants.LinkerGender;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class MemberProfileDto {
    private UserProfileDto user;
    private String nickname;
    private LinkerGender gender;
    private String introduction;
    private BigDecimal mannerTemperature;
    private LocalDate birthdate;
    private String profileImageUrl;
}
