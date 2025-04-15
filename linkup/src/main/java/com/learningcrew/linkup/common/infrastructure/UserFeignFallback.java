package com.learningcrew.linkup.common.infrastructure;

import com.learningcrew.linkup.common.dto.query.UserInfoResponse;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserFeignFallback implements UserFeignClient {

    @Override
    public UserInfoResponse getUserById(int userId) {
        throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
    }

    @Override
    public Optional<String> getEmailByUserId(int userId) {
        throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
    }

    @Override
    public Boolean existsUser(int userId) {
        return null;
    }


}