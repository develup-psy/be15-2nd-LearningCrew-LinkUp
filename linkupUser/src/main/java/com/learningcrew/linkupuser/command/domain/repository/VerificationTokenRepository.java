package com.learningcrew.linkupuser.command.domain.repository;

import com.learningcrew.linkupuser.command.domain.aggregate.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    List<VerificationToken> findAllByExpiryDateBefore(LocalDateTime expiryDateBefore);

    Optional<VerificationToken> findByCode(String tokenCode);
}
