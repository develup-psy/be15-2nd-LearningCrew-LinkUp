package com.learningcrew.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.security.SignatureException;

@Slf4j
@Component
@Order(-2) // ExceptionHandlerFilter보다 앞서 실행되게
@RequiredArgsConstructor
public class CustomGatewayExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        // 이미 커밋된 응답은 건드릴 수 없습니다.
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 응답 헤더
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        HttpStatus status;
        ErrorResponse errorResponse;

        if (ex instanceof CustomJwtException e) {
            status = e.getErrorCode().getStatus();
            errorResponse = ErrorResponse.of(e.getErrorCode());

        } else if (ex instanceof SignatureException || ex instanceof MalformedJwtException
                || ex instanceof UnsupportedJwtException || ex instanceof IllegalArgumentException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = ErrorResponse.of(ErrorCode.INVALID_JWT);

        } else if (ex instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = ErrorResponse.of(ErrorCode.EXPIRED_JWT);

        } else if (ex instanceof ConnectException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            errorResponse = ErrorResponse.of(ErrorCode.SERVICE_UNAVAILABLE, "연결할 수 없는 마이크로서비스입니다.");

        } else if (ex instanceof ResponseStatusException e) {
            HttpStatusCode statusCode = e.getStatusCode();
            status = HttpStatus.resolve(statusCode.value());
            errorResponse = ErrorResponse.of(ErrorCode.NOT_FOUND, e.getReason());

        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        response.setStatusCode(status);

        try {
            byte[] errorBytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(errorBytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error(" Error converting ErrorResponse: ", e);
            return response.setComplete();
        }
    }
}
