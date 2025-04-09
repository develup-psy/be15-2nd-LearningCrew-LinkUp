package com.learningcrew.linkup.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 인증 & 인가 관련
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "JWT 클레임이 비어있습니다."),

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    DUPLICATE_CONTACT_NUMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 번호입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 올바르지 않습니다."),

    // 토큰
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "refresh 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "refresh 토큰이 일치하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "refresh 토큰이 만료되었습니다."),

    // 모임
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 모임을 찾을 수 없습니다."),
    MEETING_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 동일한 조건의 모임이 존재합니다."),
    MEETING_PARTICIPATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "모임 최대 인원을 초과했습니다."),
    MEETING_ALREADY_JOINED(HttpStatus.BAD_REQUEST, "이미 참여 중인 모임입니다."),
    MEETING_CREATOR_CANNOT_EXIT(HttpStatus.BAD_REQUEST, "모임 생성자는 모임을 나갈 수 없습니다."),

    // 장소 예약
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 예약이 존재합니다."),
    RESERVATION_TIME_CONFLICT(HttpStatus.CONFLICT, "예약 시간이 중복됩니다."),
    RESERVATION_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "현재 예약은 취소할 수 없습니다."),

    // 신고
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "신고 내역을 찾을 수 없습니다."),
    REPORT_ALREADY_SUBMITTED(HttpStatus.BAD_REQUEST, "이미 신고한 사용자입니다."),
    REPORT_REASON_REQUIRED(HttpStatus.BAD_REQUEST, "신고 사유는 필수입니다."),

    // 알림
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),
    NOTIFICATION_ALREADY_READ(HttpStatus.BAD_REQUEST, "이미 읽은 알림입니다."),

    // 커뮤니티
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_POST_EDIT(HttpStatus.FORBIDDEN, "해당 게시글 수정 권한이 없습니다."),
    UNAUTHORIZED_COMMENT_DELETE(HttpStatus.FORBIDDEN, "해당 댓글 삭제 권한이 없습니다."),

    // 후기
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "후기를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 후기를 작성하셨습니다."),
    UNAUTHORIZED_REVIEW_EDIT(HttpStatus.FORBIDDEN, "해당 후기를 수정할 수 없습니다."),
    INVALID_REVIEW_SCORE(HttpStatus.BAD_REQUEST, "유효하지 않은 평점입니다."),

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
