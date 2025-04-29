package com.learningcrew.linkup.point.command.application.service;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
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
    @Transactional
    public PointTransactionResponse paymentTransaction(int userId, int amount) {

        // 1. 포인트 차감
        ApiResponse<Void> decreaseResponse = userFeignClient.decreasePoint(userId, amount);

        if (decreaseResponse == null || !decreaseResponse.isSuccess()) {
            throw new BusinessException(ErrorCode.POINT_DECREASE_FAILED);
        }

        // 2. 포인트 트랜잭션 기록
        PointTransaction pointTransaction = new PointTransaction(
                null,
                userId,
                -Math.abs(amount), // PAYMENT는 무조건 음수로 저장
                "PAYMENT",
                null
        );
        pointRepository.save(pointTransaction);

        // 3. 최신 포인트 조회
        int latestPointBalance = userFeignClient.getPointBalance(userId);

        return new PointTransactionResponse("포인트 결제가 완료되었습니다.", latestPointBalance);
    }
    @Transactional
    public void refundParticipationPoint(int userId, int amount) {
        // 1. 포인트 증가
        userFeignClient.increasePoint(userId, amount);

        // 2. 트랜잭션 기록
        PointTransaction pointTransaction = new PointTransaction(
                null,
                userId,
                amount, // 환불은 양수
                "REFUND",
                null
        );
        pointRepository.save(pointTransaction);
    }
    @Transactional
    public void payPlaceRentalCost(int ownerId, int amount) {
        // 1. 포인트 증가 (사업자에게 돈 줌)
        ApiResponse<Void> increaseResponse = userFeignClient.increasePoint(ownerId, amount);

        if (increaseResponse == null || !increaseResponse.isSuccess()) {
            throw new BusinessException(ErrorCode.POINT_DECREASE_FAILED, "사업자에게 장소 대여비 지급 실패");
        }

        // 2. 트랜잭션 기록 (CHARGE 타입으로 기록)
        PointTransaction pointTransaction = new PointTransaction(
                null,
                ownerId,
                amount,
                "CHARGE",
                null
        );
        pointRepository.save(pointTransaction);
    }
    @Transactional
    public void revokePlaceRentalCost(int ownerId, int amount) {
        // 1. 포인트 감소 (사업자에게 돈 뺏음)
        ApiResponse<Void> decreaseResponse = userFeignClient.decreasePoint(ownerId, amount);

        if (decreaseResponse == null || !decreaseResponse.isSuccess()) {
            throw new BusinessException(ErrorCode.POINT_DECREASE_FAILED, "사업자에게서 장소 대여비 회수 실패");
        }

        // 2. 트랜잭션 기록 (REFUND 타입)
        PointTransaction pointTransaction = new PointTransaction(
                null,
                ownerId,
                -amount,  // 차감이니까 음수
                "REFUND",
                null
        );
        pointRepository.save(pointTransaction);
    }

    @Transactional
    public void refundExtraPoint(int userId, int refundAmount) {
        System.out.println("[DEBUG] 모임 강제완료 환불 시작: userId=" + userId + ", 환불금액=" + refundAmount);

        // 1. 포인트 증가
        ApiResponse<Void> response = userFeignClient.increasePoint(userId, refundAmount);
        if (response == null || !response.isSuccess()) {
            System.out.println("[ERROR] 포인트 증가 실패: userId=" + userId);
            throw new BusinessException(ErrorCode.POINT_INCREASE_FAILED, "포인트 복구 실패");
        }
        System.out.println("[DEBUG] 포인트 증가 성공");

        // 2. 트랜잭션 기록
        PointTransaction pointTransaction = new PointTransaction(
                null,
                userId,
                refundAmount,
                "REFUND",
                null
        );
        pointRepository.save(pointTransaction);
        System.out.println("[DEBUG] 트랜잭션 저장 완료");
    }


}
