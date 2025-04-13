package com.learningcrew.linkup.linker.command.application.service;

import com.learningcrew.linkup.linker.command.application.dto.ProfileUpdateRequest;
import com.learningcrew.linkup.linker.command.application.dto.request.UserCreateRequest;
import com.learningcrew.linkup.linker.command.application.dto.request.WithdrawUserRequest;
import com.learningcrew.linkup.linker.command.application.dto.response.RegisterResponse;
import jakarta.validation.constraints.NotBlank;

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
