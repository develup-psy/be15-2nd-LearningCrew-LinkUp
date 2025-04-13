package com.learningcrew.linkup.linker.command.application.service;

import com.learningcrew.linkup.common.domain.Status;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.domain.aggregate.Friend;
import com.learningcrew.linkup.linker.command.domain.constants.LinkerStatusType;
import com.learningcrew.linkup.linker.command.domain.repository.FriendRepository;
import com.learningcrew.linkup.linker.command.domain.repository.StatusRepository;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendCommandServiceImpl implements FriendCommandService{
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final StatusRepository statusRepository;

    @Override
    public void sendFriendRequest(int requesterId, int addresseeId) {

        // 자기 자신에게 친구 요청 불가
        if (requesterId == addresseeId) {
            throw new BusinessException(ErrorCode.CANNOT_ADD_SELF);
        }

        // 대상 존재 여부 확인
        if(!userRepository.existsById(addresseeId)){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 이미 친구 관계가 존재하는지 확인
        if(friendRepository.existsByRequesterIdAndAddresseeId(requesterId, addresseeId)){
            throw new BusinessException(ErrorCode.CANNOT_ADD_SELF);
        }

        // 상태 조회
        Status status = statusRepository.findByStatusType(LinkerStatusType.PENDING.name()).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_STATUS)
        );

        // 친구 요청 엔티티 생성
        Friend friend = Friend
                .builder()
                .requesterId(requesterId)
                .addresseeId(addresseeId)
                .status(status)
                .build();

        // 저장
        friendRepository.save(friend);
    }

    @Override
    public void acceptFriendRequest(int requesterId, int addresseeId) {

    }

    @Override
    public void rejectFriendRequest(int requesterId, int addresseeId) {

    }

    @Override
    public void deleteFriend(int requesterId, int addresseeId) {

    }
}
