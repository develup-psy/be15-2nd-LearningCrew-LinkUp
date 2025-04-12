package com.learningcrew.linkup.report.command.infrastructure.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.Blacklist;
import com.learningcrew.linkup.report.command.domain.repository.BlacklistRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface JpaBlacklistRepository extends JpaRepository<Blacklist, Long>, BlacklistRepository {

    boolean existsByMemberId(Integer memberId);

    Optional<Blacklist> findByMemberId(Integer memberId);
}
