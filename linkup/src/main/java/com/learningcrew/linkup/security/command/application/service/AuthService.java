package com.learningcrew.linkup.security.command.application.service;

import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.security.command.application.dto.request.LoginRequest;
import com.learningcrew.linkup.security.command.application.dto.response.TokenResponse;
import com.learningcrew.linkup.security.command.domain.aggregate.RefreshToken;
import com.learningcrew.linkup.security.command.domain.repository.RefreshtokenRepository;
import com.learningcrew.linkup.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshtokenRepository refreshtokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {

        // 이메일로 회원 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                    () -> new BadCredentialsException("올바르지 않은 Id 혹은 비밀번호")
                );

        // 비밀번호 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("올바르지 않은 Id 혹은 비밀번호");
        }

        //토큰 발급
        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getRoleName());

        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole().getRoleName());

        //refresh token 저장
        RefreshToken refreshTokenEntity = RefreshToken
                .builder()
                .userEmail(user.getEmail())
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshTokenExpiration()))
                .build();

        refreshtokenRepository.save(refreshTokenEntity);


        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
