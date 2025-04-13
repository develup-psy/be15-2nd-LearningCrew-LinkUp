package com.learningcrew.linkup.linker.command.domain.repository;

import com.learningcrew.linkup.linker.command.domain.aggregate.Friend;
import com.learningcrew.linkup.linker.command.domain.aggregate.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    Optional<Friend> findById(FriendId friendId);

    boolean existsById(FriendId friendId);
}
