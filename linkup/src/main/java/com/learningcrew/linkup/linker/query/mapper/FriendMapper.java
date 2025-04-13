package com.learningcrew.linkup.linker.query.mapper;

import com.learningcrew.linkup.linker.query.dto.query.FriendInfoDTO;
import com.learningcrew.linkup.linker.query.dto.query.FriendRequestStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {

    List<FriendInfoDTO> findAcceptedFriends(@Param("memberId") int memberId);

    List<FriendRequestStatusDTO> findIncomingFriendRequests(@Param("memberId") int memberId);

    List<FriendRequestStatusDTO> findOutgoingFriendRequests(@Param("memberId") int memberId);

    boolean existsFriendRequest(@Param("requesterId") int requesterId, @Param("adresseeId") int adresseeId);
}

