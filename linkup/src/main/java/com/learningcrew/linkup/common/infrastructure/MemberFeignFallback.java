package com.learningcrew.linkup.common.infrastructure;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.dto.query.MeetingMemberDto;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;

public class MemberFeignFallback implements MemberQueryClient {

    @Override
    public ApiResponse<MeetingMemberDto> getMemberById(int memberId) {
        throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
    }

    @Override
    public ApiResponse<Void> updateMannerTemperature(int memberId, double mannerTemperature) {
        throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
    }
}
