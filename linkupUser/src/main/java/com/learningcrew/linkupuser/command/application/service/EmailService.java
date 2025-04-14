package com.learningcrew.linkupuser.command.application.service;

public interface EmailService {
    void sendVerificationCode(int userId, String email, String userName, String verificationCode);
}

