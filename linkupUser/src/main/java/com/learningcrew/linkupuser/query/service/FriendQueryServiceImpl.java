package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.query.dto.query.FriendInfoDto;
import com.learningcrew.linkupuser.query.dto.query.FriendRequestStatusDto;
import com.learningcrew.linkupuser.query.dto.query.UserMeetingDto;
import com.learningcrew.linkupuser.query.dto.response.FriendInfoResponse;
import com.learningcrew.linkupuser.query.dto.response.FriendRequestResponse;
import com.learningcrew.linkupuser.query.mapper.FriendMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Schema
@Service
@RequiredArgsConstructor
public class FriendQueryServiceImpl implements FriendQueryService {
    private final FriendMapper friendMapper;
    private final ModelMapper modelMapper;

    /* 친구 목록 조회 */
    @Transactional(readOnly = true)
    @Override
    public List<FriendInfoResponse> getFriends(int memberId) {
        // 친구 목록 조회(ACCEPTED 상태인 친구 관계만 조회)
        List<FriendInfoDto> friendList = friendMapper.findAcceptedFriends(memberId);

        return friendList.stream().map(
                (friendInfo) -> modelMapper.map(friendInfo, FriendInfoResponse.class)
        ).toList();
    }

    /* 친구 신청 목록 조회 */
    @Transactional(readOnly = true)
    @Override
    public List<FriendRequestResponse> getReceivedRequests(int addresseeId) {
        // address_id가 회원인 목록 조회
        List<FriendRequestStatusDto> friendRequestList = friendMapper.findIncomingFriendRequests(addresseeId);
        return friendRequestList.stream().map(
                friendInfo -> FriendRequestResponse
                        .builder()
                        .requesterId(friendInfo.getTargetMemberId())
                        .requesterNickname(friendInfo.getNickname())
                        .requesterProfileImageUrl(friendInfo.getProfileImageUrl())
                        .build()
        ).toList();
    }

    /* 친구 개설 모임 조회 */
    @Transactional(readOnly = true)
    @Override
    public List<UserMeetingDto> findMeetingsCreatedByFriends(int userId) {
        // user_id가 requester_id 혹은 address_id에 포함되어 있고
        // 상태가 ACCEPTED인 친구의
        // 모임을 조회
        return friendMapper.findMeetingsCreatedByFriends(userId);
    }
}
