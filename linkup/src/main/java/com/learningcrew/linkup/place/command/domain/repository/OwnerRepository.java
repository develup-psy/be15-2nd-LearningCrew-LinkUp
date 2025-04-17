package com.learningcrew.linkup.place.command.domain.repository;

import com.learningcrew.linkup.place.command.domain.aggregate.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
}
