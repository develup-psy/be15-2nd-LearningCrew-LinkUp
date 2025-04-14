package com.learningcrew.linkupuser.query.mapper;


import com.learningcrew.linkupuser.query.dto.query.FriendInfoDto;
import com.learningcrew.linkupuser.query.dto.query.FriendRequestStatusDto;
import com.learningcrew.linkupuser.query.dto.query.UserMeetingDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendMapper {

    List<FriendInfoDto> findAcceptedFriends(int memberId);

    List<FriendRequestStatusDto> findIncomingFriendRequests(int addresseeId);

    List<UserMeetingDto> findMeetingsCreatedByFriends(int userId);
}

