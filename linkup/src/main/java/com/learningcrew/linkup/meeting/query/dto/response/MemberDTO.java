package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class MemberDTO {
    private int memberId;
    private String gender;
    private String nickname;
    private LocalDate birthDate;
    private String introduction;
    private BigDecimal mannerTemperature;
    private String profileImageUrl;
}
