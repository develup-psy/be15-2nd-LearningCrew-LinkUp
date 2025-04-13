package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.response.FriendRequestResponse;
import com.learningcrew.linkup.linker.query.dto.response.FriendInfoResponse;

import java.util.List;

public interface FriendQueryService {

    /* 친구 목록 조회 */
    List<FriendInfoResponse> getFriends(int memberId);

    /* 받은 친구 요청 목록 조회 */
    List<FriendRequestResponse> getReceivedRequests(int adresseeId);
    
}
