package com.learningcrew.linkupuser.config;

import com.learningcrew.linkupuser.security.filter.HeaderAuthenticationFilter;
import com.learningcrew.linkupuser.security.handler.RestAccessDeniedHandler;
import com.learningcrew.linkupuser.security.handler.RestAuthenticationEntryPoint;
import com.learningcrew.linkupuser.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity //컨트롤 메서드 제어 가능하도록 활성화
@EnableWebSecurity
public class SecurityConfig {
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(restAuthenticationEntryPoint)     // 인증 실패
                                .accessDeniedHandler(restAccessDeniedHandler)          // 인가 실패
                )
                // 요청 http method, url 기준으로 인증, 인가 필요 여부 설정
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/v1/users/register", "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/verify-email").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority("USER") //추후에 hasRole로 수정
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("USER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                )
                // 커스텀 인증 필터(jwt 토큰 필터)
                .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter(){
        return new HeaderAuthenticationFilter();
    }

}