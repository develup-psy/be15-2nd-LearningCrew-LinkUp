package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.BestPlayer;
import com.learningcrew.linkup.meeting.command.domain.repository.BestPlayerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBestPlayerRepository extends BestPlayerRepository, JpaRepository<BestPlayer, Integer> {

}
