package com.learningcrew.linkup.point.query.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.point.command.domain.aggregate.Account;
import com.learningcrew.linkup.point.query.dto.query.AccountFindDto;
import com.learningcrew.linkup.point.query.dto.response.AccountResponse;
import com.learningcrew.linkup.point.query.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointAccountQueryServiceImpl implements PointAccountQueryService {

    private final AccountMapper accountMapper;

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
}
