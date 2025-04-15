package com.learningcrew.linkupuser.query.mapper;


import com.learningcrew.linkupuser.command.domain.aggregate.User;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.query.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    List<UserProfileDto> findAllUsers(int roleId);

    Optional<User> findByUserEmail(String email);

    Optional<UserDeleteDto> findByUserUserId(int userId);

    List<UserMeetingDto> findUserMeetings(int userId);

    Optional<UserPointDto> findPointByUserId(int userId);

    Optional<UserMannerTemperatureDto> findUserMannerTemperature(int userId);

    UserInfoResponse findUserInfoById(int userId);

    Optional<MeetingMemberDto> findMeetingMemberById(int memberId);

    Boolean existsByUserId(int userId);

    Optional<String> findUserEmailByUserId(int userId);
}
