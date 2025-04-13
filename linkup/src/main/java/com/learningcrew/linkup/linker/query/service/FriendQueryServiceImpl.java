package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.FriendInfoDTO;
import com.learningcrew.linkup.linker.query.dto.query.FriendRequestStatusDTO;
import com.learningcrew.linkup.linker.query.dto.response.FriendRequestResponse;
import com.learningcrew.linkup.linker.query.dto.response.FriendInfoResponse;
import com.learningcrew.linkup.linker.query.mapper.FriendMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Schema
@Service
@RequiredArgsConstructor
public class FriendQueryServiceImpl implements FriendQueryService {
    private final FriendMapper friendMapper;
    private final ModelMapper modelMapper;

    @Override
    public List<FriendInfoResponse> getFriends(int memberId) {
        // 친구 목록 조회(ACCEPTED 상태인 친구 관계만 조회)
        List<FriendInfoDTO> friendList = friendMapper.findAcceptedFriends(memberId);

        return friendList.stream().map(
                (friendInfo) -> modelMapper.map(friendInfo, FriendInfoResponse.class)
        ).toList();
    }

    @Override
    public List<FriendRequestResponse> getReceivedRequests(int addresseeId) {
        // addressee_id가 회원인 목록 조회
        List<FriendRequestStatusDTO> friendRequestList = friendMapper.findIncomingFriendRequests(addresseeId);
        return friendRequestList.stream().map(
                friendInfo -> FriendRequestResponse
                        .builder()
                        .requesterId(friendInfo.getTargetMemberId())
                        .requesterNickname(friendInfo.getNickname())
                        .requesterProfileImageUrl(friendInfo.getProfileImageUrl())
                        .build()
        ).toList();
    }
}
