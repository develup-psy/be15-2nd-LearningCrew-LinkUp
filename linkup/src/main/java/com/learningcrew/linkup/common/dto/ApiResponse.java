package com.learningcrew.linkup.common.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiResponse<T> {
    private boolean success; // 요청 성공 여부
    private T data; // 실제 데이터 ( 성공 시만 사용 )
    private String errorCode; // 실패 시 에러 코드
    private String message; // 실패 시 메세지
    private LocalDateTime timestamp; // 응답 생성 시간

    // 성공 응답 생성 정적 메소드
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답 생성 정적 메소드
    public static <T> ApiResponse<T> error(String errorCode, String message) {
        // 아래 .errorCode() 부분에는 파라미터가 누락되었으므로 .errorCode(errorCode) 로 수정해야 합니다.
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}