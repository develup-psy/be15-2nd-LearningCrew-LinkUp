package com.learningcrew.gateway.filter;

import com.learningcrew.gateway.jwt.GatewayJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final GatewayJwtTokenProvider jwtTokenProvider;

    /* Reactive Gateway 에서는 WebFlux 기술이 사용 된다.
    비동기/논블로킹 특징으로 대규모 어플리케이션에서 성능적인 부분이 좋다.
    */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        jwtTokenProvider.validateToken(token);

        // 토큰에서 사용자 정보 추출
        String userId = jwtTokenProvider.getUserIdFromJwt(token);
        String role = jwtTokenProvider.getRoleFromJWT(token);

        ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();

        // 변경 된 요청 객체를 포함하는 새로운 ServerWebExchange를 생성한다.
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutateRequest).build();

        // 다음 필터로 요청 전달한다.
        return chain.filter(mutatedExchange);
    }

    /* GlobalFilter (전역 필터) 의 우선 순위를 지정한다.
    * 숫자가 작을 수록 높은 우선 순위를 가진다. */
    @Override
    public int getOrder() {
        return -1;
    }
}
