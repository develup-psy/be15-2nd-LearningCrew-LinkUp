package com.learningcrew.linkup.linker.command.application.service;

import com.learningcrew.linkup.linker.command.domain.aggregate.Member;
import com.learningcrew.linkup.linker.command.domain.aggregate.Role;
import com.learningcrew.linkup.linker.command.domain.aggregate.Status;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.MemberRepository;
import com.learningcrew.linkup.linker.command.domain.repository.RoleRepository;
import com.learningcrew.linkup.linker.command.domain.repository.StatusRepository;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.linker.command.application.dto.UserCreateRequest;
import com.learningcrew.linkup.linker.command.exception.DuplicateEmailException;
import com.learningcrew.linkup.linker.command.exception.DuplicateNicknameException;
import com.learningcrew.linkup.common.util.DefaultImageProperties;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserAccountCommandService {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final DefaultImageProperties defaultImageProperties;
    private final PasswordEncoder passwordEncoder;
    private final StatusRepository statusRepository;

    /* 유저 생성 - 회원 가입 */
    @Transactional
    public void registerUser(@Valid UserCreateRequest request) {
        User user = modelMapper.map(request, User.class);

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 체크
        if(memberRepository.existsByNickname(request.getNickname())){
            throw new DuplicateNicknameException("이미 사용중인 닉네임입니다.");
        }

        // 닉네임 자동 생성

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 권한 부여
        Role role = roleRepository.findByRoleName("USER");
        user.setRole(role);

        //상태 부여
        Status status = statusRepository.findByStatusType("PENDING");
        user.setStatus(status);

        // User 엔티티 생성 및 저장
        userRepository.save(user);

        Member member = modelMapper.map(request, Member.class);

        // 기본 프로필 이미지 저장
        member.setProfileImageUrl(defaultImageProperties.getDefaultProfileImage());

        // Member 엔티티 생성 및 저장
        member.setMemberId(user.getUserId());
        memberRepository.save(member);
    }

    /* 회원 정보 수정 - 닉네임, 휴대폰, 비밀번호 */

    /* 회원 탈퇴 */

    /* 이메일 찾기 */

    /* 이메일 마스킹처리 */

    /* 비밀번호 재설정 토큰 전송 */

    /* 토큰을 이용한 비밀번호 재설정 */

    /* 휴대폰 중복체크 */

    /* 닉네임 중복체크 */

    /* 이메일 중복체크 */

    /* 비밀번호 변경 */




}
