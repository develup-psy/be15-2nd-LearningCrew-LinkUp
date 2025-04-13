package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.response.FriendRequestResponse;
import com.learningcrew.linkup.linker.query.dto.response.FriendResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Service;

import java.util.List;

@Schema
@Service
public class FriendQueryServiceImpl implements FriendQueryService {
    @Override
    public List<FriendResponse> getFriends(int memberId) {
        return List.of();
    }

    @Override
    public List<FriendRequestResponse> getReceivedRequests(int adresseeId) {
        return List.of();
    }

    @Override
    public List<FriendRequestResponse> getSentRequests(int requesterId) {
        return List.of();
    }
}
