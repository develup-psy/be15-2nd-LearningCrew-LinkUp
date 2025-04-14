package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.command.application.dto.response.ObjectionDecisionResponse;
import com.learningcrew.linkup.report.command.domain.aggregate.Objection;
import com.learningcrew.linkup.report.command.domain.repository.ObjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectionDecisionServiceImpl implements ObjectionDecisionService {

    private final ObjectionRepository objectionRepository;
    private final PenaltyAdminService penaltyAdminService;

    @Override
    public ObjectionDecisionResponse acceptObjection(Long objectionId) {
        Objection objection = getValidObjection(objectionId);

        // 제재 철회
        penaltyAdminService.cancelPenalty(objection.getPenaltyId().longValue());

        // 상태 변경
        objection.setStatusId(2); // 승인됨
        objection.setProcessedAt(LocalDateTime.now());
        objectionRepository.save(objection);

        return buildResponse(objection, "이의 제기가 승인되었습니다.");
    }

    @Override
    public ObjectionDecisionResponse rejectObjection(Long objectionId) {
        Objection objection = getValidObjection(objectionId);

        // 상태 변경
        objection.setStatusId(3); // 거절됨
        objection.setProcessedAt(LocalDateTime.now());
        objectionRepository.save(objection);

        return buildResponse(objection, "이의 제기가 거절되었습니다.");
    }

    private Objection getValidObjection(Long objectionId) {
        Objection objection = objectionRepository.findById(objectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTION_NOT_FOUND));

        if (objection.getStatusId() != 1) {
            throw new BusinessException(ErrorCode.OBJECTION_ALREADY_PROCESSED);
        }

        return objection;
    }

    private ObjectionDecisionResponse buildResponse(Objection objection, String message) {
        return ObjectionDecisionResponse.builder()
                .objectionId(objection.getId().longValue())
                .statusId(objection.getStatusId())
                .statusName(getStatusName(objection.getStatusId()))
                .processedAt(objection.getProcessedAt())
                .message(message)
                .build();
    }

    private String getStatusName(Integer statusId) {
        return switch (statusId) {
            case 2 -> "승인됨";
            case 3 -> "거절됨";
            default -> "대기중";
        };
    }
}
