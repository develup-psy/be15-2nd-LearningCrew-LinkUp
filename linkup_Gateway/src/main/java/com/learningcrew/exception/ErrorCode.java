package com.learningcrew.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST: 잘못된 요청 */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request."),

    /* 409 CONFLICT : 서버와의 충돌 */
    NOT_SAVED(HttpStatus.CONFLICT, "Not saved."),

    /* 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청 */
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "Unauthorized."),

    /* 403 FORBIDDEN: 권한이 없는 사용자의 요청 */
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "Forbidden."),

    /* 404 NOT_FOUND: 리소스를 찾을 수 없음 */
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),

    /* 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출 */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Not allowed method."),

    /* 409 DUPLICATE_ENTRY: 이미 값이 존재 */
    CONFLICT(HttpStatus.CONFLICT, "Data Conflict"),

    /* 422 UNPROCESSABLE_ENTITY: 요청은 잘 만들어졌지만, 문법 오류로 인하여 따를 수 없습니다. */
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity"),

    /* 500 INTERNAL_SERVER_ERROR: 내부 서버 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error."),

    // 인증 & 인가 관련
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "JWT 클레임이 비어있습니다."),

    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스를 사용할 수 없습니다.");



    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}