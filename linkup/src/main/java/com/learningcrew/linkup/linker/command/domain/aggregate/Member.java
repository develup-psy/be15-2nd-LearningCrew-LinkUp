package com.learningcrew.linkup.linker.command.domain.aggregate;

import com.learningcrew.linkup.common.constants.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    private int memberId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String nickname;

    private LocalDate birthDate;

    private String introduction;
    private BigDecimal mannerTemperature = BigDecimal.ZERO;
    private String profileImageUrl;

    public void setProfileImageUrl(String defaultProfileImage) {
        this.profileImageUrl = defaultProfileImage;
    }

    public void setMemberId(int userId) {
        this.memberId = userId;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }
}
