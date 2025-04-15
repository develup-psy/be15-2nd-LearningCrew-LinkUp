package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.common.dto.query.RoleDTO;
import com.learningcrew.linkupuser.common.query.mapper.RoleMapper;
import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import com.learningcrew.linkupuser.query.dto.query.*;
import com.learningcrew.linkupuser.query.dto.response.UserListResponse;
import com.learningcrew.linkupuser.query.dto.response.UserProfileResponse;
import com.learningcrew.linkupuser.query.mapper.MemberMapper;
import com.learningcrew.linkupuser.query.mapper.UserMapper;
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

    /* 매너 온도 조회 */
    @Override
    public UserMannerTemperatureDto getMannerTemperature(int userId) {
        return memberMapper.findUserMannerTemperature(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.BAD_REQUEST)
        );
    }

    @Override
    public UserInfoResponse getUserInfo(int userId) {
        return userMapper.findUserInfoById(userId);
    }

    @Override
    public MeetingMemberDto getMeetingMember(int memberId) {
        return userMapper.findMeetingMemberById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.BAD_REQUEST)
        );
    }

    @Override
    public Boolean getExistsUser(int userId) {
        return userMapper.existsByUserId(userId);
    }

    @Override
    public Optional<String> getUserEmail(int userId) {
        return userMapper.findUserEmailByUserId(userId);
    }
}
