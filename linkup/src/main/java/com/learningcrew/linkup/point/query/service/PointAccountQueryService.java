package com.learningcrew.linkup.point.query.service;

import com.learningcrew.linkup.point.query.dto.response.AccountResponse;

public interface PointAccountQueryService {
    AccountResponse getAccount(int userId);
}