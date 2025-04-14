package com.learningcrew.linkup.linker.query.dto.response;

import com.learningcrew.linkup.linker.query.dto.query.UserProfileDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserListResponse {
    private List<UserProfileDto> userProfileDTOList;
}
