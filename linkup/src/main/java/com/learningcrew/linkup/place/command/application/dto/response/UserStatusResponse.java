package com.learningcrew.linkup.place.command.application.dto.response;

import com.learningcrew.linkup.common.domain.Status;
import lombok.Getter;

@Getter
public class UserStatusResponse {
    private Status status;  // 블랙리스트 여부
}
