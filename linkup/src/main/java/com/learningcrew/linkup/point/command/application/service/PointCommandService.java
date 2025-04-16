package com.learningcrew.linkup.point.command.application.service;

import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.dto.request.WithdrawRequest;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.domain.aggregate.Account;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.AccountRepository;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PointCommandService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public PointTransactionResponse createPointTransaction(PointTransactionRequest request) {

        String transactionType = request.getTransactionType();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        if ("CHARGE".equals(transactionType)) {
            PointTransaction pointTransaction = new PointTransaction(
                    null,
                    request.getUserId(),
                    request.getAmount(),
                    request.getTransactionType(),
                    null
            );
            pointRepository.save(pointTransaction);

            user.addPointBalance(request.getAmount());
            userRepository.save(user);
        }

        return new PointTransactionResponse("포인트가 적립되었습니다.", user.getPointBalance());
    }
    @Transactional
    public PointTransactionResponse withdrawPoint(WithdrawRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Account account = accountRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("계좌 정보 없음"));

        int refundAmount = user.getPointBalance(); // 전액 환불

        // 포인트를 0으로 만들고
        user.subtractPointBalance(refundAmount);
        userRepository.save(user);

        // 계좌 잔액에 추가
        account = Account.builder()
                .accountId(account.getAccountId())
                .userId(account.getUserId())
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .status_id(account.getStatus_id())
                .balance(account.getBalance() + refundAmount)
                .createdAt(account.getCreatedAt())
                .build();
        accountRepository.save(account);

        // 포인트 트랜잭션 기록
        pointRepository.save(new PointTransaction(
                null,
                user.getUserId(),
                -refundAmount,
                "WITHDRAW",
                null
        ));

        return new PointTransactionResponse("전액 환불 완료", 0);
    }



}
