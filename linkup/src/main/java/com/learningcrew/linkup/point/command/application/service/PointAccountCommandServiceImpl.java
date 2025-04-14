package com.learningcrew.linkup.point.command.application.service;

import com.learningcrew.linkup.common.domain.Status;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.domain.constants.LinkerStatusType;
import com.learningcrew.linkup.linker.command.domain.repository.StatusRepository;
import com.learningcrew.linkup.point.command.application.dto.request.AccountRequest;
import com.learningcrew.linkup.point.command.domain.aggregate.Account;
import com.learningcrew.linkup.point.command.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointAccountCommandServiceImpl implements PointAccountCommandService {
    private final AccountRepository accountRepository;
    private final StatusRepository statusRepository;

    @Override
    @Transactional
    public void register(int userId, AccountRequest request) {
        // 계좌 등록 여부
        if(accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTED_ACCOUNT);
        }

        // 상태 설정
        Status status = statusRepository.findByStatusType(LinkerStatusType.PENDING.name()).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_STATUS)
        );

        // 계좌 등록
        Account account = Account
                .builder()
                .userId(userId)
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .holderName(request.getHolderName())
                .status_id(status.getStatusId())
                .build();

        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void update(int userId, AccountRequest request) {
        //계좌 수정
        Account account = accountRepository.findByUserId(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)
        );

        account.update(
                request.getBankName(),
                request.getAccountNumber(),
                request.getHolderName()
        );

        accountRepository.save(account);
    }
}
