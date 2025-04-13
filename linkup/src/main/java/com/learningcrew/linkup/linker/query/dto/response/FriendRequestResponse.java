package com.learningcrew.linkup.linker.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestResponse {
    private int requesterId;
    private String requesterNickname;
    private LocalDateTime requestedAt;
}
