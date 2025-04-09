package com.learningcrew.linkup.security.command.domain.repository;

import com.learningcrew.linkup.security.command.domain.aggregate.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshtokenRepository extends JpaRepository<RefreshToken, String> {

}
