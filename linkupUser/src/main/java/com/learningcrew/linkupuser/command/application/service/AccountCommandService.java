package com.learningcrew.linkupuser.command.application.service;


import com.learningcrew.linkupuser.command.application.dto.ProfileUpdateRequest;
import com.learningcrew.linkupuser.command.application.dto.request.UserCreateRequest;
import com.learningcrew.linkupuser.command.application.dto.response.RegisterResponse;

public interface AccountCommandService {

    RegisterResponse registerUser(UserCreateRequest request);

    void withdrawUser(String password, int userId);

    void recoverUser(String email, String password);


    void updateProfile(int userId, ProfileUpdateRequest request);

    // void verifyEmail(...);
    // void updateProfile(...);
    // void deleteUser(...);
    // void sendResetToken(...);
    // void resetPassword(...);
}
