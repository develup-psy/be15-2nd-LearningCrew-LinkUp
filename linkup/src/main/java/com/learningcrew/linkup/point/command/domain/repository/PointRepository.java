package com.learningcrew.linkup.point.command.domain.repository;

import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<PointTransaction, Long> {
}
