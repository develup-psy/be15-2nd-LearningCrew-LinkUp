package com.learningcrew.linkup.linker.command.domain.repository;

import com.learningcrew.linkup.linker.command.domain.aggregate.Status;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findByStatusType(String pending);
}
