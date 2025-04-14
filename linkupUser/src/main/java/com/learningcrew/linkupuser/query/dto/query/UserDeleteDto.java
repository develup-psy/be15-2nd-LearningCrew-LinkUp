package com.learningcrew.linkupuser.query.dto.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDeleteDto {
    private int userId;
    private String password;
    private String email;
    private int statusId;
}
