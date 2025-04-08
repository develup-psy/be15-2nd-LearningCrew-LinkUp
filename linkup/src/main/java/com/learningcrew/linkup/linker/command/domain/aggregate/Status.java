package com.learningcrew.linkup.linker.command.domain.aggregate;

import jakarta.persistence.*;

@Entity(name = "UserStatus")
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;
    private String statusType;
}
