package com.learningcrew.linkupuser.command.application.service;

import com.learningcrew.linkupuser.command.domain.aggregate.Friend;
import com.learningcrew.linkupuser.command.domain.aggregate.FriendId;
import com.learningcrew.linkupuser.command.domain.constants.LinkerStatusType;
import com.learningcrew.linkupuser.command.domain.repository.FriendRepository;
import com.learningcrew.linkupuser.command.domain.repository.StatusRepository;
import com.learningcrew.linkupuser.command.domain.repository.UserRepository;
import com.learningcrew.linkupuser.common.domain.Status;
import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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

        // 회원이 이미 친구 신청했는지 확인
        FriendId friendId = new FriendId(requesterId, addresseeId);
        if(friendRepository.existsById(friendId)){
            throw new BusinessException(ErrorCode.ALREADY_SENT_FRIEND_REQUEST);
        }

        // 상대방이 회원에게 친구 신청했는지 & 이미 친구 관계인지 검사
        FriendId reverseRequestId = new FriendId(addresseeId, requesterId);
        Optional<Friend> reverseRequest = friendRepository.findById(reverseRequestId);
        if (reverseRequest.isPresent()) {
            Friend friend = reverseRequest.get();
            String statusType = friend.getStatus().getStatusType();
            if (statusType.equals(LinkerStatusType.PENDING.name())) {
                throw new BusinessException(ErrorCode.ALREADY_REQUESTED_BY_OTHER);
                // 또는 여기서 자동 수락 처리 해도 됨 (추후 선택. 일단 수락은 엔드포인트로 오는 것으로 처리)
            } else if (statusType.equals(LinkerStatusType.ACCEPTED.name())) {
                throw new BusinessException(ErrorCode.ALREADY_FRIENDED);
            }
        }

        // 상태 조회
        Status status = statusRepository.findByStatusType(LinkerStatusType.PENDING.name()).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_STATUS)
        );


        // 친구 요청 엔티티 생성
        Friend friend = Friend
                .builder()
                .id(friendId)
                .status(status)
                .build();

        // 저장
        friendRepository.save(friend);
    }

    @Override
    @Transactional
    public void acceptFriendRequest(int requesterId, int addresseeId) {

        // 친구 관계 조회
        FriendId friendId = new FriendId(requesterId, addresseeId);

        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        // 친구 관계 상태 확인
        if(!friend.getStatus().getStatusType().equals(LinkerStatusType.PENDING.name())){
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        }

        // 상태 업데이트
        Status acceptedStatus = statusRepository.findByStatusType(LinkerStatusType.ACCEPTED.name()).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_STATUS)
        );
        friend.updateStatus(acceptedStatus);

        // 응답 시간 설정
        friend.updateResponededAt(LocalDateTime.now());

        // 저장
        friendRepository.save(friend);
    }


    @Override
    @Transactional
    public void deleteFriend(int requesterId, int addresseeId) {

        Friend friend = friendRepository.findBidirectionalFriend(requesterId, addresseeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        friendRepository.delete(friend);

    }
}
