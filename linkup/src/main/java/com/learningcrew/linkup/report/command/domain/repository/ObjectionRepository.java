package com.learningcrew.linkup.report.command.domain.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.Objection;

import java.util.List;
import java.util.Optional;

public interface ObjectionRepository {

    Objection save(Objection objection);

    Optional<Objection> findById(Long id);

    List<Objection> findAll();

    List<Objection> findByStatusId(Integer statusId);

    List<Objection> findByMemberId(Integer memberId);

    boolean existsByPenaltyIdAndMemberId(Long penaltyId, Integer memberId);
}
