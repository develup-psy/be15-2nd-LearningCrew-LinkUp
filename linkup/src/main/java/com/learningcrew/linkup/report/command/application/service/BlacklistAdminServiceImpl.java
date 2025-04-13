package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.command.application.dto.request.BlacklistRegisterRequest;
import com.learningcrew.linkup.report.command.application.dto.response.BlacklistRegisterResponse;
import com.learningcrew.linkup.report.command.application.dto.response.BlacklistRemoveResponse;
import com.learningcrew.linkup.report.command.domain.aggregate.Blacklist;
import com.learningcrew.linkup.report.command.domain.repository.BlacklistRepository;
import com.learningcrew.linkup.report.command.domain.repository.ReportHistoryRepository;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BlacklistAdminServiceImpl implements BlacklistAdminService {

    private final BlacklistRepository blacklistRepository;
    private final UserRepository userRepository;
    private final ReportHistoryRepository reportRepository;

    @Override
    public BlacklistRegisterResponse registerBlacklist(BlacklistRegisterRequest request) {
        // 사용자 존재 확인
        User user = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 이미 등록 여부 확인
        if (blacklistRepository.existsByMemberId(request.getMemberId())) {
            throw new BusinessException(ErrorCode.ALREADY_BLACKLISTED);
        }

        // 블랙리스트 등록
        Blacklist saved = blacklistRepository.save(
                Blacklist.builder()
                        .memberId(request.getMemberId())
                        .reason(request.getReason())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // TODO: 사용자 상태 → 정지 (status_id = 3)
        // user.setStatusId(3); userRepository.save(user);

        // TODO: 게시글, 댓글, 후기 비공개 처리

        // TODO: 알림 설정 비활성화

        // TODO: 친구, 찜, 선호 운동 등 삭제

        // TODO: 모임 취소 및 참여 이력 처리

        // 신고 이력 처리 완료 표시
        reportRepository.markAllReportsHandledByMemberId(request.getMemberId());

        return BlacklistRegisterResponse.builder()
                .memberId(saved.getMemberId())
                .message("블랙리스트 등록이 완료되었습니다.")
                .build();
    }

    @Override
    public BlacklistRemoveResponse removeBlacklist(Integer memberId) {
        // 등록 여부 확인
        Blacklist blacklist = blacklistRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BLACKLIST_NOT_FOUND));

        // 삭제 처리
        blacklistRepository.delete(blacklist);

        return BlacklistRemoveResponse.builder()
                .memberId(memberId)
                .message("블랙리스트에서 정상적으로 해제되었습니다.")
                .build();
    }
}
