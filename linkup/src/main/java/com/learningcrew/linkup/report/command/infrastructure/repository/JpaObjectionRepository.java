package com.learningcrew.linkup.report.command.infrastructure.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.Objection;
import com.learningcrew.linkup.report.command.domain.repository.ObjectionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface JpaObjectionRepository extends JpaRepository<Objection, Long>, ObjectionRepository {

    List<Objection> findByStatusId(Integer statusId);

    List<Objection> findByMemberId(Integer memberId);

    boolean existsByPenaltyIdAndMemberId(Long penaltyId, Integer memberId);
}
