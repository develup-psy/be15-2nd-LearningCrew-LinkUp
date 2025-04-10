package com.learningcrew.linkup.linker.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "UserStatus")
@Table(name = "status")
@Getter
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;
    private String statusType;
}
