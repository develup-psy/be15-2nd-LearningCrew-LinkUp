package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.query.dto.query.UserPointDto;

public interface PaymentQueryService {

    UserPointDto findUserPoint(int userId);
}

