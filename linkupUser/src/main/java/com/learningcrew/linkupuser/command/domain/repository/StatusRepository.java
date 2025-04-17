package com.learningcrew.linkupuser.command.domain.repository;

import com.learningcrew.linkupuser.common.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findByStatusType(String name);
}
