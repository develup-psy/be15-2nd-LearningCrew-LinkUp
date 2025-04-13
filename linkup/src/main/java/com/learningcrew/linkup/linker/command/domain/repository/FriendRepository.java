package com.learningcrew.linkup.linker.command.domain.repository;

import com.learningcrew.linkup.linker.command.domain.aggregate.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    boolean existsByRequesterIdAndAddresseeId(int requesterId, int addresseeId);
}
