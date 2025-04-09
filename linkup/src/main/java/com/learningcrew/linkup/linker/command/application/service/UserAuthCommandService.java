package com.learningcrew.linkup.linker.command.application.service;

import com.learningcrew.linkup.common.exception.BusinessException;
import com.learningcrew.linkup.common.exception.CustomJwtException;
import com.learningcrew.linkup.common.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.application.dto.LoginRequest;
import com.learningcrew.linkup.linker.command.application.dto.TokenResponse;
import com.learningcrew.linkup.linker.command.domain.aggregate.RefreshToken;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.RefreshtokenRepository;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.linker.command.domain.service.TokenDomainService;
import com.learningcrew.linkup.linker.command.domain.service.UserValidatorService;
import com.learningcrew.linkup.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshtokenRepository refreshtokenRepository;
    private final UserValidatorService userValidatorService;
    private final TokenDomainService tokenDomainService;
    private final UserRepository userRepository;

    /* 이메일 로직 구현 */
    @Transactional
    public TokenResponse login(LoginRequest request) {

        // 이메일로 회원 조회
        User user = userValidatorService.validateEmail(request.getEmail());

        // 비밀번호 확인
        userValidatorService.validatePassword(request.getPassword(), user.getPassword());

        //토큰 발급
        String accessToken = tokenDomainService.generateToken(user);
        String refreshToken = tokenDomainService.generateRefreshToken(user);

        //refresh token 저장
        tokenDomainService.saveRefreshToken(user.getEmail(), refreshToken);

        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /* 토큰 재발급 구현 */
    public TokenResponse refreshToken(String providedRefreshToken) {
        jwtTokenProvider.validateToken(providedRefreshToken);
        String email = jwtTokenProvider.getEmailFromJWT(providedRefreshToken);

        RefreshToken storedToken = refreshtokenRepository.findById(email).orElseThrow(()->new CustomJwtException(ErrorCode.USER_NOT_FOUND));

        if(!storedToken.getToken().equals(providedRefreshToken)) {
            throw new CustomJwtException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        if(storedToken.getExpiryDate().before(new Date())) {
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT);
        }

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        //토큰 발급
        String accessToken = tokenDomainService.generateToken(user);
        String refreshToken = tokenDomainService.generateRefreshToken(user);

        //refresh token 저장
        tokenDomainService.saveRefreshToken(user.getEmail(), refreshToken);

        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /* 로그아웃 기능 구현 */
    public void logout(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String email = jwtTokenProvider.getEmailFromJWT(refreshToken);
        refreshtokenRepository.deleteById(email);
    }
}
