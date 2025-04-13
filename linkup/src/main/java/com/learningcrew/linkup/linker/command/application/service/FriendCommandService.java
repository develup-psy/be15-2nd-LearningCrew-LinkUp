package com.learningcrew.linkup.linker.command.application.service;

public interface FriendCommandService {

    /* 친구 요청 */
    void sendFriendRequest(int requesterId, int addresseeId);

    /* 친구 요청 수락 */
    void acceptFriendRequest(int requesterId, int addresseeId);

    /* 친구 삭제 */
    void deleteFriend(int requesterId, int addresseeId);

}
