package com.learningcrew.linkup.config;

import com.learningcrew.linkup.security.jwt.JwtAuthenticationFilter;
import com.learningcrew.linkup.security.jwt.JwtTokenProvider;
import com.learningcrew.linkup.security.handler.RestAccessDeniedHandler;
import com.learningcrew.linkup.security.handler.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
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
                .authorizeHttpRequests(auths -> {
                    permitAllEndpoints(auths);
                    userEndpoints(auths);
                    adminEndpoints(auths);
                    sharedAuthEndpoints(auths);
                    businessEndpoints(auths);

                    auths.anyRequest().authenticated();
                })
                // 커스텀 인증 필터(jwt 토큰 필터)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    /* 공통 api */
    private void permitAllEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/v1/users/register",
                "/api/v1/auth/login",
                "/api/v1/auth/password/reset-link",
                "/api/v1/auth/password/reset",
                "/api/v1/users/recover",
                "/api/v1/auth/refresh",
                "/api/v1/auth/verify-email"
        ).permitAll();
    }

    /* 사용자 접근 api */
    private void userEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/api/v1/users/me",
                "/api/v1/users/me/profile",
                "/api/v1/users/me/manner-temperature",
                "/api/v1/users/me/posts",
                "/api/v1/users/me/comments",
                "/api/v1/users/me/meetings/participated",
                "/api/v1/users/me/point",
                "/api/v1/accounts/**",
                "/api/v1/auth/logout",
                "/api/v1/users/withdraw"
        ).hasAuthority("USER");

        auths.requestMatchers(
                "/api/v1/meetings/**",
                "/api/v1/friends/**",
                "/api/v1/posts/**",
                "/api/v1/comments/**",
                "/api/v1/postComment/**",
                "/api/v1/payments/**",
                "/api/v1/settlements/**",
                "/api/v1/place/**",
                "/api/v1/places/**",
                "/api/v1/owner/**",
                "/api/v1/owner/**",
                "/api/v1/members/**",
                "/api/v1/user/**",
                "/api/v1/meetings/**",
                "/api/v1/businesses/me"
        ).hasAuthority("USER");

        auths.requestMatchers(HttpMethod.POST, "/api/v1/businesses").hasAuthority("USER");
    }

    /* 사업자 api */
    private void businessEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                HttpMethod.PUT, "/api/v1/businesses"
        ).hasAuthority("BUSINESS");
    }

    /* 관리자 api */
    private void adminEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/api/v1/report/**",
                "/api/v1/report/**",
                "/api/v1/penalty/**",
                "/api/v1/objections/**",
                "/api/v1/objections/**",
                "/api/v1/blacklist/**"
        ).hasAuthority("ADMIN");

        auths.requestMatchers(
                "/api/v1/admin/**"
        ).hasAuthority("ADMIN");

    }

    /* 인증만 필요 api */
    private void sharedAuthEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "api/v1/report/**",
                "api/v1/penalty/**",
                "api/v1/objections/**",
                "api/v1/blacklist/**"
        ).authenticated();
    }







}