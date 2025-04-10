package com.learningcrew.linkup.linker.command.domain.service;

import com.learningcrew.linkup.common.constants.StatusType;
import com.learningcrew.linkup.common.exception.BusinessException;
import com.learningcrew.linkup.common.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.domain.aggregate.Role;
import com.learningcrew.linkup.linker.command.domain.aggregate.Status;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.RoleRepository;
import com.learningcrew.linkup.linker.command.domain.repository.StatusRepository;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /* 비밀번호 암호화 */
    @Transactional
    public void encryptPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    /* 권한 설정 */
    @Transactional
    public void assignRole(User user, String roleName) {
        Role role = roleRepository.findByRoleName(roleName);
        user.setRole(role);
    }

    /* 초기 상태 설정 */
    @Transactional
    public void assignStatus(User user, String statusName) {
        Status status = statusRepository.findByStatusType(statusName).orElseThrow(
                ()-> new BusinessException(ErrorCode.INVALID_STATUS)
        );
        user.setStatus(status);
    }

    /* 회원 삽입 */
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }


    public boolean existsByUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void activateUser(User user) {
        Status activeStatus = statusRepository.findByStatusType(StatusType.ACCEPTED.name()).orElseThrow(
                ()-> new BusinessException(ErrorCode.INVALID_STATUS)
        );
        user.setStatus(activeStatus);
        userRepository.save(user);
    }
}
