package com.learningcrew.linkupuser.query.service;



import com.learningcrew.linkupuser.query.dto.query.UserMeetingDto;
import com.learningcrew.linkupuser.query.dto.response.FriendInfoResponse;
import com.learningcrew.linkupuser.query.dto.response.FriendRequestResponse;

import java.util.List;

public interface FriendQueryService {

    /* 친구 목록 조회 */
    List<FriendInfoResponse> getFriends(int memberId);

    /* 받은 친구 요청 목록 조회 */
    List<FriendRequestResponse> getReceivedRequests(int adresseeId);

    List<UserMeetingDto> findMeetingsCreatedByFriends(int userId);
}
