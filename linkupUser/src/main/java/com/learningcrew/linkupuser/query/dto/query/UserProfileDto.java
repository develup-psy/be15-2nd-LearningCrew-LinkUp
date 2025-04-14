package com.learningcrew.linkupuser.query.dto.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private int userId;
    private String userName;
    private String email;
    private int pointBalance;
    private String contactNumber;
}
