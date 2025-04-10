package com.learningcrew.linkup.place.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Date;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Column(name = "userId")
    private int memberId;

    private String gender;
    private String nickname;
    private Date birthday;
    private String introduction;
    private double memberTemperature;
    private String profileImageUrl;
}
