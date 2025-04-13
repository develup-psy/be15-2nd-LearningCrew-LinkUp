package com.learningcrew.linkup.linker.query.mapper;

import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.query.dto.query.UserDeleteDto;
import com.learningcrew.linkup.linker.query.dto.query.UserMeetingDto;
import com.learningcrew.linkup.linker.query.dto.query.UserProfileDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    List<UserProfileDto> findAllUsers(int roleId);

    Optional<User> findByUserEmail(String email);

    Optional<UserDeleteDto> findByUserUserId(int userId);

    List<UserMeetingDto> findUserMeetings(int userId);
}
