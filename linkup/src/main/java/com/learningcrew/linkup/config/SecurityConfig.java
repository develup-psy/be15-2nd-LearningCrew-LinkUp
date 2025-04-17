package com.learningcrew.linkup.config;

import com.learningcrew.linkup.security.filter.HeaderAuthenticationFilter;
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
                .authorizeHttpRequests(auths -> {
                    permitAllEndpoints(auths);
                    userEndpoints(auths);
                    adminEndpoints(auths);
                    sharedAuthEndpoints(auths);
                    businessEndpoints(auths);

                    auths.anyRequest().authenticated();
                })
                // 커스텀 인증 필터(jwt 토큰 필터)
                .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter(){
        return new HeaderAuthenticationFilter();
    }

    /* 공통 api */
    private void permitAllEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/users/register",
                "/auth/login",
                "/auth/password/reset-link",
                "/auth/password/reset",
                "/users/recover",
                "/auth/refresh",
                "/auth/verify-email"
        ).permitAll();
    }

    /* 사용자 접근 api */
    private void userEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/users/me",
                "/users/me/profile",
                "/users/me/manner-temperature",
                "/users/me/posts",
                "/users/me/comments",
                "/users/me/meetings/participated",
                "/users/me/point",
                "/accounts/**",
                "/auth/logout",
                "/users/withdraw"
        ).hasAuthority("USER");

        auths.requestMatchers(
                "/friends/**",
                "/posts/**",
                "/comments/**",
                "/postComment/**",
                "/payments/**",
                "/place/**",
                "/places/**",
                "/owner/**",
                "/owner/**",
                "/members/**",
                "/user/**",
                "/businesses/me",
                "/objections/review/{reivewId}",
                "/objections/post/{postId}",
                "/objections/comment/{commentId}"
        ).hasAuthority("USER");

        auths.requestMatchers(HttpMethod.POST, "/businesses").hasAuthority("USER");
    }

    /* 사업자 api */
    private void businessEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                HttpMethod.PUT, "/businesses"
        ).hasAuthority("BUSINESS");

        auths.requestMatchers(
            "/settlements/**",
            "/place",
            "/place/:id",
            "/place/:id/times",
            "/place/:id/images",
                "/owner/{ownerId}/places"
        ).hasAuthority("BUSINESS");
    }

    /* 관리자 api */
    private void adminEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
            "/report",
            "/report/reportType/{reportTypeId}",
            "/report/reporter-user",
            "/report/reportee-user",
            "/report/reporter-user/{reportedId}",
            "/report/reportee-user/{reporteeId}",
            "/report/{reportId}/rejected",
            "/report/{reportId}/accepted",
            "/penalty",
            "/penalty/{penaltyType}",
            "/penalty/user/{memberId}",
            "/penalty/user/{memberId}/{penaltyType}",
            "/penalty/post/{postId}",
            "/penalty/comment/{commentId}",
            "/penalty/placeReview/{reviewId}",
            "/penalty/placerRview/{reviewId}/done",
            "/penalty/{penaltyId}",
            "/objections",
            "/objections/status/{statusId}",
            "/objections/user/{memberId}",
            "/objections/{objectionId}/accept",
            "/objections/{objectionId}/reject",
            "/blacklist",
            "/blacklist/{memberId}",
            "/blacklist/{memberId}/clear",
            "/posts/list",
            "/post/user/{userId}",
            "/admin/users",
            "/admin/businesses/pending",
            "/admin/businesses/{businessId}/approve",
            "/admin/businesses/{businessId}/reject",
            "/meetings/list",
            "/meetings/review",
            "/meetings/review/reviewer/{memberId}",
            "/meetings/review/reviewee/{memberId}",
            "/admin/places"
        ).hasAuthority("ADMIN");

        auths.requestMatchers(
                "/admin/**"
        ).hasAuthority("ADMIN");

    }

    /* 다중 권한 api */
    private void sharedAuthEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        //회원 OR 관리자
        auths.requestMatchers(
                "/meetings",
                "/meetings",
                "/meetings/user/{userId}",
                "/meetings/user/{userId}/done",
                "/meetings/{meetingId}/participation",
                "/posts",
                "/posts/{postId}",
                "/posts/{postId}/delete",
                "/postComment",
                "/postComment/{postCommentId}"
        ).hasAuthority("ADMIN");

        //사업자 OR 관리자
        auths.requestMatchers(
                "/owner/{ownerId}/reserve",
                "/owner/{ownerId}/reserve"
        ).hasAuthority("ADMIN");

        //사업자 OR ADMIN

    }







}