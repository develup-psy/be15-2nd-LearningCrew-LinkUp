package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkup.linker.query.dto.response.UserListResponse;
import com.learningcrew.linkup.linker.query.dto.response.UserProfileResponse;

public interface UserQueryService {
    UserProfileResponse getUserProfile(int userId);
    UserListResponse getUserList();
    int getRoleIdByRoleName(String roleName);
    UserMannerTemperatureDto getMannerTemperature(int userId);
}
