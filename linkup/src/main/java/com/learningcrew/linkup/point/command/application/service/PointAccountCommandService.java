package com.learningcrew.linkup.point.command.application.service;

import com.learningcrew.linkup.point.command.application.dto.request.AccountRequest;

public interface PointAccountCommandService {
    void register(int userId, AccountRequest request);
    void update(int userId, AccountRequest request);
}
