package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.linker.query.dto.query.UserPointDto;
import com.learningcrew.linkup.linker.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final UserMapper userMapper;

    /* 포인트 조회 */
    @Override
    public UserPointDto findUserPoint(int userId) {
        return userMapper.findPointByUserId(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.BAD_REQUEST)
        );
    }
}
