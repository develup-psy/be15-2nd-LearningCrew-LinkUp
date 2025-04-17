package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.query.MeetingMemberDto;
import com.learningcrew.linkupuser.query.dto.query.UserInfoResponse;
import com.learningcrew.linkupuser.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkupuser.query.dto.response.UserListResponse;
import com.learningcrew.linkupuser.query.dto.response.UserProfileResponse;
import com.learningcrew.linkupuser.query.dto.response.UserStatusResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserQueryService {
    UserProfileResponse getUserProfile(int userId);
    UserListResponse getUserList();
    int getRoleIdByRoleName(String roleName);
    UserMannerTemperatureDto getMannerTemperature(int userId);

    UserInfoResponse getUserInfo(int userId);

    MeetingMemberDto getMeetingMember(int memberId);

    Boolean getExistsUser(int userId);

    String getUserEmail(int userId);

    String getUserNameByUserId(int userId);

    int getPointBalance(int userId);

    UserStatusResponse getUserStatus(int userId);
}
