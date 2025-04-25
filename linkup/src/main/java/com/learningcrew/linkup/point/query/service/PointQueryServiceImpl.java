package com.learningcrew.linkup.point.query.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.point.query.dto.query.AccountFindDto;
import com.learningcrew.linkup.point.query.dto.response.AccountResponse;
import com.learningcrew.linkup.point.query.dto.response.MonthlySettlementResponse;
import com.learningcrew.linkup.point.query.dto.response.PointHistoryResponse;
import com.learningcrew.linkup.point.query.dto.response.SettlementDetailResponse;
import com.learningcrew.linkup.point.query.mapper.AccountMapper;
import com.learningcrew.linkup.point.query.mapper.PointTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointQueryServiceImpl implements PointQueryService {

    private final AccountMapper accountMapper;
    private final PointTransactionMapper pointTransactionMapper;

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(int userId) {
        AccountFindDto account = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));

        return AccountResponse.builder()
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .build();
    }

    @Override
    public List<PointHistoryResponse> getPointHistory(int userId) {
        return pointTransactionMapper.findPointHistoriesByUserId(userId);
    }

    @Override
    public List<SettlementDetailResponse> getAllSettlements(int userId) {
        return pointTransactionMapper.findAllByOwnerId(userId);
    }

    @Override
    public MonthlySettlementResponse getMonthlySettlement(int ownerId, int year, int month) {
        return pointTransactionMapper.findMonthlySettlementAmount(ownerId, year, month);
    }
}
