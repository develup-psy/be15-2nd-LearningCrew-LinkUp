package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.common.query.mapper.StatusMapper;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.place.command.application.dto.request.OwnerRegisterRequest;
import com.learningcrew.linkup.place.command.application.dto.response.UserStatusResponse;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Owner;
import com.learningcrew.linkup.place.command.domain.constants.OwnerStatus;
import com.learningcrew.linkup.place.command.domain.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OwnerCommandServiceImpl implements OwnerCommandService {

    private final OwnerRepository ownerRepository;
    private final StatusMapper statusMapper;
    private final UserFeignClient userFeignClient;

    /* 사업자 등록 신청 */
    @Override
    @Transactional
    public void registerBusiness(int ownerId, OwnerRegisterRequest request) {
        //사업자 신청 중복 검사
        if (ownerRepository.existsById(ownerId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_OWNER_REGISTRATION);
        }

        int pendingStatusId = statusMapper.statusByStatusType(OwnerStatus.PENDING.name()).orElseThrow(
                ()->new BusinessException(ErrorCode.INVALID_STATUS)
        ).getStatusId();

        Owner owner = Owner
                .builder()
                .ownerId(ownerId)
                .businessRegistrationDocumentUrl(request.getBusinessRegistrationDocumentUrl())
                .statusId(pendingStatusId)
                .build();
        ownerRepository.save(owner);
    }

    /* 사업자 등록 승인 */
    @Override
    @Transactional
    public void approveBusiness(int ownerId) {

        // 사업자 활성화 상태 확인
        UserStatusResponse userStatus = userFeignClient.getUserStatus(ownerId).getData();
        if (!userStatus.getStatus().getStatusType().equals(OwnerStatus.ACCEPTED.name())) {
            throw new BusinessException(ErrorCode.WITHDRAW_USER);
        }

        // 사업자 등록 신청 승인 상태 부여
        int approvedStatusId = statusMapper.statusByStatusType(OwnerStatus.ACCEPTED.name()).orElseThrow(
                ()->new BusinessException(ErrorCode.INVALID_STATUS)
        ).getStatusId();

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OWNER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        owner.approve(now, approvedStatusId);
        ownerRepository.save(owner);
    }

    /* 사업자 등록 거절 */
    @Override
    @Transactional
    public void rejectBusiness(int ownerId, String rejectionReason) {

        // 사업자 활성화 상태 확인
        UserStatusResponse userStatus = userFeignClient.getUserStatus(ownerId).getData();
        if (!userStatus.getStatus().getStatusType().equals(OwnerStatus.ACCEPTED.name())) {
            throw new BusinessException(ErrorCode.WITHDRAW_USER);
        }

        // 사업자 등록 신청 거절 상태 부여
        int rejectedStatusId = statusMapper.statusByStatusType(OwnerStatus.REJECTED.name()).orElseThrow(
                ()->new BusinessException(ErrorCode.INVALID_STATUS)
        ).getStatusId();

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OWNER_NOT_FOUND));

        owner.reject(rejectionReason, rejectedStatusId);
        ownerRepository.save(owner);
    }
}