package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.MemberProfileDTO;
import com.learningcrew.linkup.linker.query.dto.response.UserProfileResponse;
import com.learningcrew.linkup.linker.query.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final MemberMapper memberMapper;

    public UserProfileResponse getUserProfile(String email) {
        MemberProfileDTO memberProfileDTO = Optional.ofNullable(memberMapper.getUserProfileByEmail(email))
                .orElseThrow(() -> new RuntimeException("유저 정보 찾지 못함"));

        return UserProfileResponse
                .builder()
                .member(memberProfileDTO)
                .build();
    }
}
