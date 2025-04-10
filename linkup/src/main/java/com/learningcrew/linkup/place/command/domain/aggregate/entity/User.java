package com.learningcrew.linkup.place.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private int roleId;
    private int statusId;
    private String userName;
    private String password;
    private String contactNumber;
    private String email;
    private int pointBalance;
    @CreatedDate
    private Date dateCreated;
    @LastModifiedDate
    private Date dateUpdated;
}
