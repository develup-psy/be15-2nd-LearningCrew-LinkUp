package com.learningcrew.linkup.linker.command.application.service;

import com.learningcrew.linkup.linker.command.application.dto.LoginRequest;
import com.learningcrew.linkup.linker.command.application.dto.TokenResponse;
import com.learningcrew.linkup.linker.command.domain.aggregate.RefreshToken;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.RefreshtokenRepository;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.security.jwt.JwtTokenProvider;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserAuthCommandService {
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

    public TokenResponse refreshToken(String providedRefreshToken) {
        jwtTokenProvider.validateToken(providedRefreshToken);
        String email = jwtTokenProvider.getEmailFromJWT(providedRefreshToken);
        String role = jwtTokenProvider.getRoleFromJWT(providedRefreshToken);

        RefreshToken storedToken = refreshtokenRepository.findById(email).orElseThrow(()->new BadCredentialsException("해당 유저로 조회되는 리프레시 토큰 없음"));

        if(!storedToken.getToken().equals(providedRefreshToken)) {
            throw new BadCredentialsException("refresh 토큰 일치하지 않음");
        }

        if(storedToken.getExpiryDate().before(new Date())) {
            throw new BadCredentialsException("refresh Token 유효시간 만료");
        }

        //토큰 발급
        String accessToken = jwtTokenProvider.createToken(email, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(email, role);

        //refresh token 저장
        RefreshToken refreshTokenEntity = RefreshToken
                .builder()
                .userEmail(email)
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

    /* 로그아웃 */
    public void logout(@NotBlank(message = "refreshToken을 포함해주세요") String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String email = jwtTokenProvider.getEmailFromJWT(refreshToken);
        refreshtokenRepository.deleteById(email);
    }
}
