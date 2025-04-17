package com.learningcrew.linkupuser.query.mapper;


import com.learningcrew.linkupuser.query.dto.query.MemberProfileDto;
import com.learningcrew.linkupuser.query.dto.query.UserMannerTemperatureDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    MemberProfileDto getUserProfileByEmail(int userId);

    Optional<UserMannerTemperatureDto> findUserMannerTemperature(int userId);
}
