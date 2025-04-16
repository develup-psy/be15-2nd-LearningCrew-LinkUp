package com.learningcrew.linkup.point.command.application.service;

import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PointCommandService {
    private final PointRepository pointRepository;
    private final UserFeignClient userFeignClient;

    @Transactional
    public PointTransactionResponse createPointTransaction(PointTransactionRequest request) {

        String transactionType = request.getTransactionType();

        // 먼저 User 서비스에 포인트 증가 요청
        userFeignClient.increasePoint(request.getUserId(), request.getAmount());


        // 테이블에 거래 이력 저장
        if ("CHARGE".equals(transactionType)) {
            PointTransaction pointTransaction = new PointTransaction(
                    null,
                    request.getUserId(),
                    request.getAmount(),
                    request.getTransactionType(),
                    null
            );
            pointRepository.save(pointTransaction);
        }

        // 최신 포인터 정보 조회
        int latestPointBalance = userFeignClient.getPointBalance(request.getUserId());

        return new PointTransactionResponse("포인트가 적립되었습니다.", latestPointBalance);
    }
}
