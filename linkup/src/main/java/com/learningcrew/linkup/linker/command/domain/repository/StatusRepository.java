package com.learningcrew.linkup.linker.command.domain.repository;

import com.learningcrew.linkup.linker.command.domain.aggregate.Status;
import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Status findByStatusType(String pending);
}
