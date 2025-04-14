package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkupuser.query.dto.response.UserListResponse;
import com.learningcrew.linkupuser.query.dto.response.UserProfileResponse;

public interface UserQueryService {
    UserProfileResponse getUserProfile(int userId);
    UserListResponse getUserList();
    int getRoleIdByRoleName(String roleName);
    UserMannerTemperatureDto getMannerTemperature(int userId);
}
