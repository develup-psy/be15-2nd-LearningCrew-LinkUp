package com.learningcrew.linkup.point.command.application.service;

import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.dto.request.WithdrawRequest;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.domain.aggregate.Account;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

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

    @Transactional
    public PointTransactionResponse withdrawPoint(WithdrawRequest request) {


        Account account = accountRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("계좌 정보 없음"));

        int userId = request.getUserId();
        int refundAmount = userFeignClient.getPointBalance(request.getUserId());

        // 포인트를 0으로 만들고
        userFeignClient.decreasePoint(userId, refundAmount);

        // 계좌 잔액에 추가
        account = Account.builder()
                .accountId(account.getAccountId())
                .userId(account.getUserId())
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .status_id(account.getStatus_id())
                .createdAt(account.getCreatedAt())
                .build();
        accountRepository.save(account);

        // 포인트 트랜잭션 기록
        pointRepository.save(new PointTransaction(
                null,
                userId,
                -refundAmount,
                "WITHDRAW",
                null
        ));
        return new PointTransactionResponse("전액 환불 완료", 0);
    }
}
