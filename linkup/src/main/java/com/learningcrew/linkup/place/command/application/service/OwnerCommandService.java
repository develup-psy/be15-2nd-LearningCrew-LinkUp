package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.place.command.application.dto.request.OwnerRegisterRequest;

import java.time.LocalDateTime;

public interface OwnerCommandService {
    void registerBusiness(int ownerId, OwnerRegisterRequest request);
    void approveBusiness(int ownerId);
    void rejectBusiness(int ownerId, String rejectionReason);
}
