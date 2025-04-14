package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.common.dto.query.RoleDTO;
import com.learningcrew.linkup.common.query.mapper.RoleMapper;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;

import com.learningcrew.linkup.linker.command.domain.aggregate.User;

import com.learningcrew.linkup.linker.query.dto.query.MemberProfileDto;
import com.learningcrew.linkup.linker.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkup.linker.query.dto.query.UserProfileDto;

import com.learningcrew.linkup.linker.query.dto.response.UserListResponse;
import com.learningcrew.linkup.linker.query.dto.response.UserProfileResponse;
import com.learningcrew.linkup.linker.query.mapper.MemberMapper;
import com.learningcrew.linkup.linker.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final MemberMapper memberMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public UserProfileResponse getUserProfile(int userId) {
        MemberProfileDto memberProfileDTO = Optional.ofNullable(memberMapper.getUserProfileByEmail(userId))
                .orElseThrow(() -> new RuntimeException("유저 정보 찾지 못함"));

        return UserProfileResponse
                .builder()
                .member(memberProfileDTO)
                .build();
    }

    @Override
    public UserListResponse getUserList() {
        int roleId = getRoleIdByRoleName("USER");
        List<UserProfileDto> userProfileDTOList = userMapper.findAllUsers(roleId);
        return UserListResponse
                .builder()
                .userProfileDTOList(userProfileDTOList)
                .build();
    }

    @Override
    public int getRoleIdByRoleName(String roleName) {
        RoleDTO role = roleMapper.roleByRoleName(roleName).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_ROLE)
        );
        return role.getRoleId();
    }


    public Optional<User> getUserName(Integer userId) {
        return Optional.ofNullable(userMapper.findUserNameByUserId(userId))
                .orElseThrow(() -> new RuntimeException("사용자 이름을 찾을 수 없습니다."));

    /* 매너 온도 조회 */
    @Override
    public UserMannerTemperatureDto getMannerTemperature(int userId) {
        return memberMapper.findUserMannerTemperature(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.BAD_REQUEST)
        );
    }
}
