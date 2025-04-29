package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.place.command.application.dto.request.OperationTimeRequest;
import com.learningcrew.linkup.place.command.application.dto.response.OperationTimeUpdateResponse;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.OperationTime;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.command.domain.repository.OperationTimeRepository;
import com.learningcrew.linkup.place.command.domain.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationTimeCommandService {

    private final OperationTimeRepository operationTimeRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public OperationTimeUpdateResponse updateOperationTimes(int placeId, List<OperationTimeRequest> operationTimeRequests) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        int currentUserId = getCurrentUserId(); // 👈 로그인한 유저 ID 가져오기

        if (place.getOwnerId() != currentUserId) {
            throw new BusinessException(ErrorCode.NO_PERMISSION); // 👈 기존에 있는 NO_PERMISSION 에러 사용
        }


        for (OperationTimeRequest req : operationTimeRequests) {
            if (req.getStartTime().equals(req.getEndTime())) {
                throw new BusinessException(ErrorCode.INVALID_OPERATION_TIME);
            }
            if (req.getEndTime().isBefore(req.getStartTime())) {
                throw new BusinessException(ErrorCode.INVALID_OPERATION_TIME);
            }

            List<OperationTime> existingList = operationTimeRepository.findByPlaceIdAndDayOfWeek(placeId, req.getDayOfWeek());

            if (!existingList.isEmpty()) {
                // ✅ 수정
                OperationTime existing = existingList.get(0);
                existing.setStartTime(req.getStartTime());
                existing.setEndTime(req.getEndTime());
                existing.setUnitTime(req.getUnitTime());
                operationTimeRepository.save(existing);
            } else {
                // ✅ 새로 생성
                OperationTime newOp = new OperationTime();
                newOp.setPlaceId(placeId);
                newOp.setDayOfWeek(req.getDayOfWeek());
                newOp.setStartTime(req.getStartTime());
                newOp.setEndTime(req.getEndTime());
                newOp.setUnitTime(req.getUnitTime());
                operationTimeRepository.save(newOp);
            }
        }

        return OperationTimeUpdateResponse.builder()
                .message("운영시간이 정상적으로 수정되었습니다.")
                .build();
    }

    private void validateOperationTimes(List<OperationTimeRequest> requests) {
        for (OperationTimeRequest req : requests) {
            if (req.getStartTime().equals(req.getEndTime())) {
                throw new BusinessException(ErrorCode.INVALID_OPERATION_TIME);
            }
            if (req.getEndTime().isBefore(req.getStartTime())) {
                throw new BusinessException(ErrorCode.INVALID_OPERATION_TIME);
            }
            if (req.getUnitTime() == 0 || req.getUnitTime() <= 0) {
                throw new BusinessException(ErrorCode.INVALID_UNIT_TIME);
            }
            if (!isValidDayOfWeek(req.getDayOfWeek())) {
                throw new BusinessException(ErrorCode.INVALID_DAY_OF_WEEK);
            }
            if (req.getUnitTime() % 10 != 0) {
                throw new BusinessException(ErrorCode.INVALID_UNIT_TIME);
            }
        }
    }

    private boolean isValidDayOfWeek(String dayOfWeek) {
        return List.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").contains(dayOfWeek);
    }
    private int getCurrentUserId() {
        return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
