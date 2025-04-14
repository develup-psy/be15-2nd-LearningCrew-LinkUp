package com.learningcrew.linkup.linker.query.mapper;

import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.query.dto.query.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    List<UserProfileDto> findAllUsers(int roleId);

    Optional<User> findByUserEmail(String email);

    Optional<User> findUserNameByUserId(int userId);

    Optional<UserDeleteDto> findByUserUserId(int userId);

    List<UserMeetingDto> findUserMeetings(int userId);

    Optional<UserPointDto> findPointByUserId(int userId);

    Optional<UserMannerTemperatureDto> findUserMannerTemperature(int userId);

}
