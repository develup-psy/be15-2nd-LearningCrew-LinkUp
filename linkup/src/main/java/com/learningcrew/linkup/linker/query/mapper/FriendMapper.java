package com.learningcrew.linkup.linker.query.mapper;

import com.learningcrew.linkup.linker.query.dto.query.FriendInfoDTO;
import com.learningcrew.linkup.linker.query.dto.query.FriendRequestStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {

    List<FriendInfoDTO> findAcceptedFriends(int memberId);

    List<FriendRequestStatusDTO> findIncomingFriendRequests(int addresseeId);
}

