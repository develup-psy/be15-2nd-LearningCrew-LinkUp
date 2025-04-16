package com.learningcrew.linkup.common.infrastructure;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.dto.query.UserInfoResponse;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
public class UserFeignFallback implements UserFeignClient {

    private final Throwable cause;

    public UserFeignFallback(Throwable cause) {
        this.cause = cause;
    }

    private void handleError(String methodName) {
        log.error("[UserFeignFallback] {} 호출 실패 - {}", methodName, cause.toString(), cause);

        if (cause instanceof FeignException.NotFound) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        } else if (cause instanceof FeignException.ServiceUnavailable) {
            throw new BusinessException(ErrorCode.USER_SERVICE_UNAVAILABLE);
        } else if (cause instanceof FeignException.Unauthorized) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        throw new BusinessException(ErrorCode.FEIGN_CLIENT_ERROR);
    }

    @Override
    public ApiResponse<Void> increasePoint(int userId, int amount) {
        handleError("increasePoint");
        return ApiResponse.failure("포인트 증가 실패 (Fallback)");
    }

    @Override
    public int getPointBalance(int userId) {
        handleError("getPointBalance");
        return -1; // 기본값
    }

    @Override
    public ApiResponse<Void>  decreasePoint(int userId, int amount) {
        handleError("decreasePoint");
        return ApiResponse.failure("포인트 감소 실패 (Fallback)");
    }

    @Override
    public UserInfoResponse getUserById(int userId) {
        handleError("getUserById");
        return null;
    }

    @Override
    public String getEmailByUserId(int userId) {
        handleError("getEmailByUserId");
        return null;
    }

    @Override
    public Boolean existsUser(int userId) {
        handleError("existsUser");
        return false;
    }

    @Override
    public String getUserNameByUserId(int userId) {
        handleError("getUserNameByUserId");
        return "알 수 없음";
    }
}