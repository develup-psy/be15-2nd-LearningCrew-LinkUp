package com.learningcrew.linkup.report.command.domain.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.Blacklist;

import java.util.Optional;

public interface BlacklistRepository {

    Blacklist save(Blacklist blacklist);

    void delete(Blacklist blacklist);

    boolean existsByMemberId(Integer memberId);

    Optional<Blacklist> findByMemberId(Integer memberId);
}
