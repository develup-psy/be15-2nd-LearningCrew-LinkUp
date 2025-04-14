package com.learningcrew.linkup.security.service;

import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.exception.security.CustomJwtException;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String stringUserId) throws UsernameNotFoundException {
        int intUserId = Integer.parseInt(stringUserId);
        User user = userRepository.findById(intUserId)
                .orElseThrow(() -> new CustomJwtException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(), //보안상 암호화된 비밀번호
                user.getRole().getRoleName()
        );
    }
}
