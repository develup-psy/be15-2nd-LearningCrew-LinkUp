package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserPointDto;

public interface PaymentQueryService {

    UserPointDto findUserPoint(Long userId);
}

