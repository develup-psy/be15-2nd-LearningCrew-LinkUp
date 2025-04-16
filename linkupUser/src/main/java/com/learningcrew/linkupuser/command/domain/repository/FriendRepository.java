package com.learningcrew.linkupuser.command.domain.repository;


import com.learningcrew.linkupuser.command.domain.aggregate.Friend;
import com.learningcrew.linkupuser.command.domain.aggregate.FriendId;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    Optional<Friend> findById(FriendId friendId);

    boolean existsById(FriendId friendId);

    void deleteById(FriendId friendId);

    @Query("SELECT f FROM Friend f WHERE (f.id.requesterId = :requesterId AND f.id.addresseeId = :addresseeId) " +
            "OR (f.id.requesterId = :addresseeId AND f.id.addresseeId = :requesterId)")
    Optional<Friend> findBidirectionalFriend(@Param("requesterId") int requesterId,
                                             @Param("addresseeId") int addresseeId);
}
