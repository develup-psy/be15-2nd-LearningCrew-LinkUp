package com.learningcrew.linkup.security.command.domain.repository;

import com.learningcrew.linkup.security.command.domain.aggregate.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshtokenRepository extends JpaRepository<RefreshToken, String> {
}
