package com.learningcrew.linkupuser.command.domain.service;


import com.learningcrew.linkupuser.command.domain.aggregate.User;

public interface TokenDomainService {
    String generateToken(User user);
    String generateRefreshToken(User user);
    void saveRefreshToken(int userId, String email, String refreshToken);
}
