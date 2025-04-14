package com.learningcrew.linkupuser.command.domain.repository;


import com.learningcrew.linkupuser.command.domain.aggregate.Friend;
import com.learningcrew.linkupuser.command.domain.aggregate.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    Optional<Friend> findById(FriendId friendId);

    boolean existsById(FriendId friendId);

    void deleteById(FriendId friendId);
}
