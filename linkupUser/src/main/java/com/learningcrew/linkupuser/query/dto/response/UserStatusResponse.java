package com.learningcrew.linkupuser.query.dto.response;

import com.learningcrew.linkupuser.common.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponse {
    private Status status;
}