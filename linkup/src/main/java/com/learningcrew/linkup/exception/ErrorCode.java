package com.learningcrew.linkup.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    PLACE_NOT_FOUND("10001", "장소가 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_OPERATION_TIME("10002","종료시간은 시작시간과 동일하거나 그 전일 수 없습니다.",HttpStatus.BAD_REQUEST),

    // 파일 관련 오류
    FILE_SAVE_ERROR("20001","파일 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_ERROR("20002","파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 입력 값 검증 오류
    VALIDATION_ERROR("40001","입력 값 검증 오류입니다.",HttpStatus.BAD_REQUEST),

    // 그 외 기타 오류
    INTERNAL_SERVER_ERROR("50000","내부 서버 오류입니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    FAVORITE_ALREADY_EXISTS("409001", "이미 등록된 즐겨찾기입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
