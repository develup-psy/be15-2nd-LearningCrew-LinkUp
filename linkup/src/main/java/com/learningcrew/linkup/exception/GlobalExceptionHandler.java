package com.learningcrew.linkup.exception;

import com.learningcrew.linkup.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex) {
        // ErrorCode에서 설정한 HttpStatus를 사용할 수도 있습니다.
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getErrorCode().getCode(), ex.getMessage()));
    }

    // 다른 예외 핸들러들...
}

