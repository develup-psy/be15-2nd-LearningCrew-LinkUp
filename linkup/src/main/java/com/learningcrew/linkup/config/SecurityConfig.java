package com.learningcrew.linkup.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.POST, "/api/v1/users/register", "/api/v1/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/me").hasAuthority("USER") //추후에 hasRole로 수정
                                .requestMatchers(HttpMethod.GET, "/api/v1/admin").hasAuthority("ADMIN")
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }

}
